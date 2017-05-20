package spacerace.gameengine;

import spacerace.client.RemoteGame;
import spacerace.client.communication.LocalServerAdapter;
import spacerace.domain.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class MyB00bGameEngine implements SpaceRaceGameEngine {

    private Vector2D startPosition;

    private double maxSpeed = 0.2;
    private double maxStabalizeSpeed = 0.1;
    private boolean dieOnPurpose = false;

    private LocalDateTime lastRestart = LocalDateTime.now();

    private Direction heading = Direction.UP;
    private Direction previousHeading = Direction.UP;

    @Override
    public Action getAction(final ShipState shipState) {
        if (startPosition == null) {
            startPosition = shipState.getPosition();
        }
        else if (sameVector(shipState.getPosition(), startPosition)) {
            if (lastRestart.plusSeconds(2).isBefore(LocalDateTime.now())) {
                heading = Direction.UP;
                previousHeading = Direction.UP;
                lastRestart = LocalDateTime.now();
                if (dieOnPurpose) {
                    dieOnPurpose = false;
                }
                else {
                    heading = Direction.UP;
                    previousHeading = Direction.UP;
                    maxSpeed -= 0.01;
                }
            }
        }

        if (shipState.isPassedGoal()) {
            return Action.withAcceleration(Acceleration.NONE, Acceleration.NONE);
        }

        setBestHeading(shipState);

        if (dieOnPurpose) {
            final Acceleration yAcc = shipState.getSpeed().getY() >= 0 ? Acceleration.POSITIVE : Acceleration.NEGATIVE;
            final Acceleration xAcc = shipState.getSpeed().getX() >= 0 ? Acceleration.POSITIVE : Acceleration.NEGATIVE;

            return Action.withAcceleration(xAcc, yAcc);
        }

        final Acceleration stabilizationAcceleration =  getStabilizationSpeed(shipState.getSpeed(), shipState.getDetectors());
        final ArrayList<Detector> frontDetectors = getDetectors(shipState.getDetectors(), heading);

        final boolean sightForwardIsFree = frontDetectors.stream().allMatch(detector -> detector.getBeam() == null);

        // System.out.println("Heading: " + heading + " PreviousHeading: " + previousHeading);

        switch (heading) {
            case LEFT:
                if (shipState.getSpeed().getX() > -maxSpeed && sightForwardIsFree || shipState.getSpeed().getX() > 0.01) {
                    return Action.withAcceleration(Acceleration.NEGATIVE, stabilizationAcceleration);
                }
                else if (shipState.getSpeed().getX() < -0.01) {
                    return Action.withAcceleration(Acceleration.POSITIVE, stabilizationAcceleration);
                }
                else {
                    return Action.withAcceleration(Acceleration.NONE, stabilizationAcceleration);
                }
            case RIGHT:
                if (shipState.getSpeed().getX() < maxSpeed && sightForwardIsFree || shipState.getSpeed().getX() < -0.01) {
                    return Action.withAcceleration(Acceleration.POSITIVE, stabilizationAcceleration);
                }
                else if (shipState.getSpeed().getX() > 0.01) {
                    return Action.withAcceleration(Acceleration.NEGATIVE, stabilizationAcceleration);
                }
                else {
                    return Action.withAcceleration(Acceleration.NONE, stabilizationAcceleration);
                }
            case UP:
                if (shipState.getSpeed().getY() > -maxSpeed && sightForwardIsFree || shipState.getSpeed().getY() > 0.01) {
                    return Action.withAcceleration(stabilizationAcceleration, Acceleration.NEGATIVE);
                }
                else if (shipState.getSpeed().getY() < -0.01) {
                    return Action.withAcceleration(stabilizationAcceleration, Acceleration.POSITIVE);
                }
                else {
                    return Action.withAcceleration(stabilizationAcceleration, Acceleration.NONE);
                }
            case DOWN:
                if (shipState.getSpeed().getY() < maxSpeed && sightForwardIsFree || shipState.getSpeed().getY() < -0.01) {
                    return Action.withAcceleration(stabilizationAcceleration, Acceleration.POSITIVE);
                }
                else if (shipState.getSpeed().getY() > 0.01) {
                    return Action.withAcceleration(stabilizationAcceleration, Acceleration.NEGATIVE);
                }
                else {
                    return Action.withAcceleration(stabilizationAcceleration, Acceleration.NONE);
                }
        }

        return Action.withAcceleration(Acceleration.NONE, Acceleration.NONE);
    }

    private Acceleration getStabilizationSpeed(final Vector2D speed, final List<Detector> detectors) {
        final double speedToStabilize;
        final ArrayList<Detector> frontDetectors = getDetectors(detectors, heading);
        final Detector leftFrontDetector = frontDetectors.get(0);
        final Detector rightFrontDetector = frontDetectors.get(2);
        final ArrayList<Detector> leftSideDetectors;
        final ArrayList<Detector> rightSideDetectors;
        final Optional<Integer> leftSideDistance;
        final Optional<Integer> rightSideDistance;

        final boolean rightIsPositive;

        switch (heading) {
            case LEFT:
                speedToStabilize = speed.getY();
                leftSideDetectors = getDetectors(detectors, Direction.DOWN);
                rightSideDetectors = getDetectors(detectors, Direction.UP);

                rightIsPositive = false;
                break;
            case RIGHT:
                speedToStabilize = speed.getY();
                leftSideDetectors = getDetectors(detectors, Direction.UP);
                rightSideDetectors = getDetectors(detectors, Direction.DOWN);

                rightIsPositive = true;
                break;
            case UP:
                speedToStabilize = speed.getX();
                leftSideDetectors = getDetectors(detectors, Direction.LEFT);
                rightSideDetectors = getDetectors(detectors, Direction.RIGHT);

                rightIsPositive = true;
                break;
            case DOWN:
            default:
                speedToStabilize = speed.getX();
                leftSideDetectors = getDetectors(detectors, Direction.RIGHT);
                rightSideDetectors = getDetectors(detectors, Direction.LEFT);

                rightIsPositive = false;
                break;
        }

        leftSideDistance = getShortestBeam(leftSideDetectors);
        rightSideDistance = getShortestBeam(rightSideDetectors);

        final double normalizedSpeed = Math.abs(speedToStabilize);

        if (leftFrontDetector.getBeam() != null && normalizedSpeed < maxStabalizeSpeed) {
            return rightIsPositive ? Acceleration.POSITIVE : Acceleration.NEGATIVE;
        }
        if (rightFrontDetector.getBeam() != null && normalizedSpeed < maxStabalizeSpeed) {
            return rightIsPositive ? Acceleration.NEGATIVE : Acceleration.POSITIVE;
        }

        if (leftSideDistance.isPresent() && rightSideDistance.isPresent() && leftSideDistance.get() > rightSideDistance.get() && normalizedSpeed < maxStabalizeSpeed) {
            return rightIsPositive ? Acceleration.NEGATIVE : Acceleration.POSITIVE;
        }
        if (rightSideDistance.isPresent() && leftSideDistance.isPresent() && rightSideDistance.get() > leftSideDistance.get() && normalizedSpeed < maxStabalizeSpeed) {
            return rightIsPositive ? Acceleration.POSITIVE : Acceleration.NEGATIVE;
        }

        if (speedToStabilize > 0.01) {
            return Acceleration.NEGATIVE;
        }
        if (speedToStabilize < -0.01) {
            return Acceleration.POSITIVE;
        }

        return Acceleration.NONE;
    }

    private ArrayList<Detector> getDetectors(final List<Detector> detectors, final Direction direction) {
        final ArrayList<Detector> frontDetectors = new ArrayList<>();
        switch (direction) {
            case LEFT:
                frontDetectors.addAll(Arrays.asList(detectors.get(2), detectors.get(1), detectors.get(0)));
                break;
            case RIGHT:
                frontDetectors.addAll(detectors.subList(3, 6));
                break;
            case UP:
                frontDetectors.addAll(detectors.subList(6, 9));
                break;
            case DOWN:
            default:
                frontDetectors.addAll(Arrays.asList(detectors.get(11), detectors.get(10), detectors.get(9)));
                break;
        }

        return frontDetectors;
    }

    private Optional<Integer> getShortestBeam(final List<Detector> detectors) {
        final Optional<Detector> shortestDetector = detectors.stream().filter(detector -> detector.getBeam() != null).min(Comparator.comparing(Detector::getBeamLength));
        if (shortestDetector.isPresent()) {
            return Optional.of(shortestDetector.get().getBeamLength());
        }
        else {
            return Optional.empty();
        }
    }

    private ArrayList<Detector> getLeftSideDetectors(final List<Detector> detectors) {
        final ArrayList<Detector> leftSideDetectors;
        switch (heading) {
            case UP:
                leftSideDetectors = getDetectors(detectors, Direction.LEFT);
                break;
            case DOWN:
                leftSideDetectors = getDetectors(detectors, Direction.RIGHT);
                break;
            case LEFT:
                leftSideDetectors = getDetectors(detectors, Direction.DOWN);
                break;
            case RIGHT:
            default:
                leftSideDetectors = getDetectors(detectors, Direction.UP);
                break;
        }

        Collections.reverse(leftSideDetectors);
        return leftSideDetectors;
    }

    private ArrayList<Detector> getRightSideDetectors(final List<Detector> detectors) {
        switch (heading) {
            case UP:
                return getDetectors(detectors, Direction.RIGHT);
            case DOWN:
                return getDetectors(detectors, Direction.LEFT);
            case LEFT:
                return getDetectors(detectors, Direction.UP);
            case RIGHT:
            default:
                return getDetectors(detectors, Direction.DOWN);
        }
    }

    private void setBestHeading(ShipState shipState) {
        final HashSet<Direction> availableDirections = getAvailableDirections(shipState.getDetectors());
        final ArrayList<Detector> frontDetectors = getDetectors(shipState.getDetectors(), heading);
        final ArrayList<Detector> leftSideDetectors = getLeftSideDetectors(shipState.getDetectors());
        final ArrayList<Detector> rightSideDetectors = getRightSideDetectors(shipState.getDetectors());

        if (frontDetectors.stream().allMatch(detector -> detector.getBeam() != null) || leftSideDetectors.get(0).getBeam() == null && leftSideDetectors.get(1).getBeam() != null ||
                rightSideDetectors.get(0).getBeam() == null && rightSideDetectors.get(1).getBeam() != null) {
            setPreferredHeading(availableDirections);
        }
    }

    private void setPreferredHeading(final HashSet<Direction> availableDirections) {
        switch (heading) {
            case LEFT:
                if (!availableDirections.contains(Direction.LEFT)) {
                    if (availableDirections.contains(Direction.UP) && (previousHeading != Direction.DOWN || !availableDirections.contains(Direction.DOWN))) {
                        setNewHeading(Direction.UP);
                    } else if (availableDirections.contains(Direction.DOWN)) {
                        setNewHeading(Direction.DOWN);
                    } else if (availableDirections.contains(Direction.RIGHT)) {
                        setNewHeading(Direction.RIGHT);
                    }
                } else {
                    if (availableDirections.contains(Direction.UP) && previousHeading != Direction.DOWN) {
                        setNewHeading(Direction.UP);
                    }
                }
                break;
            case RIGHT:
                if (!availableDirections.contains(Direction.RIGHT)) {
                    if (availableDirections.contains(Direction.DOWN) && (previousHeading != Direction.UP || !availableDirections.contains(Direction.UP))) {
                        setNewHeading(Direction.DOWN);
                    } else if (availableDirections.contains(Direction.UP)) {
                        setNewHeading(Direction.UP);
                    } else if (availableDirections.contains(Direction.LEFT)) {
                        setNewHeading(Direction.LEFT);
                    }
                } else {
                    if (availableDirections.contains(Direction.DOWN) && previousHeading != Direction.UP) {
                        setNewHeading(Direction.DOWN);
                    }
                }
                break;
            case UP:
                if (!availableDirections.contains(Direction.UP)) {
                    if (availableDirections.contains(Direction.RIGHT) && (previousHeading != Direction.LEFT || !availableDirections.contains(Direction.LEFT))) {
                        setNewHeading(Direction.RIGHT);
                    } else if (availableDirections.contains(Direction.LEFT)) {
                        setNewHeading(Direction.LEFT);
                    } else if (availableDirections.contains(Direction.DOWN)) {
                        setNewHeading(Direction.DOWN);
                    }
                } else {
                    if (availableDirections.contains(Direction.RIGHT) && previousHeading != Direction.LEFT) {
                        setNewHeading(Direction.RIGHT);
                    }
                }
                break;
            case DOWN:
                if (!availableDirections.contains(Direction.DOWN)) {
                    if (availableDirections.contains(Direction.LEFT) && (previousHeading != Direction.RIGHT || !availableDirections.contains(Direction.RIGHT))) {
                        setNewHeading(Direction.LEFT);
                    } else if (availableDirections.contains(Direction.RIGHT)) {
                        setNewHeading(Direction.RIGHT);
                    } else if (availableDirections.contains(Direction.UP)) {
                        setNewHeading(Direction.UP);
                    }
                } else {
                    if (availableDirections.contains(Direction.LEFT) && previousHeading != Direction.RIGHT) {
                        setNewHeading(Direction.LEFT);
                    }
                }
                break;
        }
    }

    private void setNewHeading(final Direction newHeading) {
        switch (heading) {
            case UP:
                previousHeading = Direction.UP;
                break;
            case DOWN:
                previousHeading = Direction.DOWN;
                break;
            case LEFT:
                previousHeading = Direction.LEFT;
                break;
            case RIGHT:
            default:
                previousHeading = Direction.RIGHT;
                break;
        }

        heading = newHeading;
    }

    private HashSet<Direction> getAvailableDirections(final List<Detector> detectors) {
        final ArrayList<Direction> availableDirections = new ArrayList<>();
        availableDirections.add(Direction.UP);
        availableDirections.add(Direction.UP);
        availableDirections.add(Direction.UP);
        availableDirections.add(Direction.DOWN);
        availableDirections.add(Direction.DOWN);
        availableDirections.add(Direction.DOWN);
        availableDirections.add(Direction.LEFT);
        availableDirections.add(Direction.LEFT);
        availableDirections.add(Direction.LEFT);
        availableDirections.add(Direction.RIGHT);
        availableDirections.add(Direction.RIGHT);
        availableDirections.add(Direction.RIGHT);

        for (final Detector detector : detectors) {
            if (detector.getBeam() != null) {
                switch (detector.getDetectorPosition()) {
                    case UP:
                        availableDirections.remove(Direction.UP);
                        break;
                    case DOWN:
                        availableDirections.remove(Direction.DOWN);
                        break;
                    case LEFT:
                        availableDirections.remove(Direction.LEFT);
                        break;
                    case RIGHT:
                        availableDirections.remove(Direction.RIGHT);
                        break;
                }
            }
        }
        HashSet<Direction> directions = new HashSet<>();
        for (final Direction availableDirection : availableDirections) {
            directions.add(availableDirection);
        }

        return directions;
    }

    private boolean sameVector(final Vector2D vector1, final Vector2D vector2) {
        return vector1.toString().equals(vector2.toString());
    }

    public static void main(final String[] args) throws IOException, InterruptedException {
        final String              playerName   = "Robocop";
        final String              gameName     = "Battle of Trustly";
        final int                 levelNumber  = 2;
        final LocalServerAdapter  server       = new LocalServerAdapter(playerName, gameName, levelNumber);
        final RemoteGame          remoteGame   = new RemoteGame(server, playerName, gameName, levelNumber);
        final SpaceRaceGameEngine myGameEngine = new MyB00bGameEngine();
        remoteGame.runGame(myGameEngine);
    }
}