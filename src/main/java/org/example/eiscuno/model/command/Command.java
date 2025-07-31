package org.example.eiscuno.model.command;
/**
 * Represents a command in the command pattern.
 * Implementations encapsulate an action or request that can be executed.
 */
public interface Command {
    /**
     * Executes the encapsulated action.
     */
    void execute();
}