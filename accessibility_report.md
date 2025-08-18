# Accessibility Report for MindTrack

This document provides an accessibility evaluation of **MindTrack**, a wellness and productivity tracking application, through the lens of the **7 Principles of Universal Design**, as well as E3I-based inclusivity analysis.

---

## Target Users

MindTrack is designed for:

- University students and working professionals
- Individuals managing mental wellness alongside daily productivity
- Users who want to identify correlations between mood, tasks, and events
- People who value goal-setting, habit formation, and data-driven feedback

The interface is structured to support daily task management, wellness logging, event journaling, and AI-based productivity feedback. These users often prefer systems that are intuitive, minimally disruptive, and support long-term mental health tracking.

---

## Users Who May Struggle with Access

Using an **E3I framework**, we recognize that certain users may face disproportionate challenges interacting with the system:

**Group**: Users with cognitive disabilities (e.g., ADHD, anxiety disorders, short-term memory limitations)

**E3I Analysis**:
- **Equity**: These users may not have equal access to focus-sustaining tools (e.g., clear task workflows or input validation).
- **Empowerment**: The complexity of some UI sections (like multiple task panels or repeated fields) may undermine users' sense of control.
- **Inclusion**: Without sufficient visual cues, auditory alerts, or reminders, these users may miss key interactions (e.g., logging wellness entries or completing overdue tasks).
- **Intersectionality**: A student who has anxiety and is also managing multiple part-time jobs may struggle due to time fragmentation and higher cognitive load.

---

## Evaluation: The 7 Principles of Universal Design

### 1. Equitable Use

**Definition**: The design is useful and accessible to people with diverse abilities, without stigmatization.

**Implementation in MindTrack**:
- Wellness logging, goal tracking, and task editing are available to all users equally.
- No "separate" experience for people with disabilities, and the system avoids language or design that stigmatizes users.

**Opportunities**:
- Add a high-contrast mode or dyslexia-friendly font for better visual accessibility.
- Add keyboard navigation and screen reader compatibility for users with motor or visual impairments.

---

### 2. Flexibility in Use

**Definition**: The design accommodates a wide range of individual preferences and abilities.

**Implementation in MindTrack**:
- Tasks and wellness entries can be edited, deleted, or re-logged at different times of day.
- Users can customize their notification times to suit different daily routines.

**Opportunities**:
- Allow users to choose between visual (chart-based) or textual summaries of wellness history.
- Add alternative interaction methods (e.g., voice logging or reminder tones).

---

### 3. Simple and Intuitive Use

**Definition**: The use of the design is easy to understand, regardless of the user’s experience or concentration level.

**Implementation in MindTrack**:
- Modules are clearly labeled (Tasks, Events, Goals, Wellness Log).
- Input fields are intuitive: users can create/edit/delete without needing a tutorial.
- Mood tracking uses familiar categories like “Energy,” “Stress,” “Fatigue.”

**Opportunities**:
- Combine overlapping UI sections (e.g., separate panels for “Add Task” and “Create Task”) to reduce confusion.
- Add tooltips or short guides for buttons like “Export” or “Add with Yesterday.”

---

### 4. Perceptible Information

**Definition**: The design communicates information effectively to all users regardless of sensory abilities.

**Implementation in MindTrack**:
- Text-based notifications and field labels are visible and structured.
- Mood and task categories are visually distinct.

**Opportunities**:
- Introduce redundant cues (e.g., icons + text + color coding).
- Support text-to-speech or screen reader labels on input fields.

---

### 5. Tolerance for Error

**Definition**: The design minimizes hazards and the consequences of accidental actions.

**Implementation in MindTrack**:
- All major objects (task, event, goal, wellness entry) include an **Edit** function to correct mistakes.
- The `Clear` button allows users to reset forms before submission.

**Opportunities**:
- Add confirmation dialogs for destructive actions like “Delete.”
- Provide an "Undo" snackbar for accidental removals or edits.

**Discussion Focus**:  
This principle is especially relevant to users with anxiety, ADHD, or memory-related conditions. The ability to recover from errors increases user confidence and reduces stress.

---

### 6. Low Physical Effort

**Definition**: The design can be used efficiently and comfortably with minimal fatigue.

**Implementation in MindTrack**:
- UI allows most actions with few clicks.
- Wellness logging is limited to three quick entries per day.

**Opportunities**:
- Add keyboard shortcuts for common actions.
- Reduce scrolling by collapsing sections not in use.

---

### 7. Size and Space for Approach and Use

**Definition**: Appropriate size and space are provided for interaction regardless of user’s posture, mobility, or device.

**Implementation in MindTrack**:
- Buttons are adequately spaced and clickable.
- UI is desktop-optimized with clear layout zones.

**Opportunities**:
- Improve mobile or tablet responsiveness.
- Ensure resizable windows and components for assistive tech users.

---

## Conclusion

MindTrack demonstrates strong alignment with universal design values through editable components, intuitive structures, and flexible routines. However, future iterations can better support cognitive diversity by minimizing visual redundancy, confirming destructive actions, and incorporating accessible design components.

This evaluation encourages inclusive development practices aligned with both Universal Design and E3I principles, supporting a broader and more equitable user base.
