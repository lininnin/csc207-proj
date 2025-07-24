package entity;

/**
 * Interface for entities that can be marked as one-time use.
 * Implemented by Tasks and Events but not Goals.
 * Follows Interface Segregation Principle - entities only implement if they need this functionality.
 */
public interface OneTimeTrackable {
    /**
     * Checks if this entity is marked for one-time use.
     * One-time entities are removed from Available lists after the day ends.
     *
     * @return true if this is a one-time entity, false otherwise
     */
    boolean isOneTime();
}