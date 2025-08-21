package use_case.alex.event_related.todays_events_module.edit_todays_event;

import entity.alex.Event.EventInterf;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Interactor for editing today's events.
 * Now fully decoupled from the concrete Event class using EventInterf.
 */
public class EditTodaysEventInteractor implements EditTodaysEventInputBoundary {

    private final EditTodaysEventDataAccessInterf dataAccess;
    private final EditTodaysEventOutputBoundary presenter;

    public EditTodaysEventInteractor(EditTodaysEventDataAccessInterf dataAccess,
                                     EditTodaysEventOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(EditTodaysEventInputData inputData) {
        String eventId = inputData.getId();
        String dueDateStr = inputData.getDueDate();

        try {
            // Step 1: 校验 dueDate 格式
            LocalDate dueDate;
            try {
                dueDate = LocalDate.parse(dueDateStr);
            } catch (DateTimeParseException e) {
                presenter.prepareFailView(new EditTodaysEventOutputData(eventId, "", true));
                return;
            }

            // Step 2: 获取原始事件（接口类型）
            EventInterf event = dataAccess.getEventById(eventId);
            if (event == null) {
                presenter.prepareFailView(new EditTodaysEventOutputData(eventId, dueDateStr, true));
                return;
            }

            // Step 3: 修改 dueDate
            event.getBeginAndDueDates().setDueDate(dueDate);

            // Step 4: 写入数据库
            boolean success = dataAccess.update(event);
            if (!success) {
                presenter.prepareFailView(new EditTodaysEventOutputData(eventId, dueDateStr, true));
                return;
            }

            // Step 5: 成功视图
            presenter.prepareSuccessView(new EditTodaysEventOutputData(eventId, dueDateStr, false));

        } catch (Exception e) {
            e.printStackTrace();
            presenter.prepareFailView(new EditTodaysEventOutputData(eventId, dueDateStr, true));
        }
    }
}
