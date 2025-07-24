# Goal Entity Refactoring TODO

## Current Issues
The Goal entity currently has a direct dependency on the old Task entity:
- Field: `private final Task targetTask;`
- Constructor parameter: `Task targetTask`

## Required Changes

1. **Update Goal Entity**
    - Change `targetTask` field to either:
        - Option A: `String targetTaskId` (loose coupling, recommended)
        - Option B: `AvailableTask targetTask` (if direct reference needed)

2. **Update Goal Constructor**
   ```java
   // Current
   public Goal(Info info, BeginAndDueDates dates, Task targetTask, TimePeriod timePeriod, int frequency)
   
   // Should be
   public Goal(Info info, BeginAndDueDates dates, String targetTaskId, TimePeriod timePeriod, int frequency)
   ```

## Benefits of String ID Approach
- Loose coupling between entities
- Tasks can be deleted without breaking goals
- Easier serialization
- Follows aggregate boundary principles

## Migration Strategy
1. Update Goal to use taskId
2. Create migration script for existing data
3. Update all Goal usages
4. Remove old Task import from Goal