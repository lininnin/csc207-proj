package use_case.Alex.avaliable_events_module.edit_event;

import entity.info.Info;

public class EditEventInteractor implements EditEventInputBoundary {

    private final EditEventDataAccessInterf dataAccess;
    private final EditEventOutputBoundary presenter;

    public EditEventInteractor(EditEventDataAccessInterf dataAccess,
                               EditEventOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(EditEventInputData inputData) {
        String eventId = inputData.getId();
        String name = inputData.getName();
        String category = inputData.getCategory();
        String description = inputData.getDescription();

        try {
            // ✅ Step 1: 输入合法性校验
            if (name == null || name.trim().isEmpty()) {
                presenter.prepareFailView(new EditEventOutputData(eventId, "", "", "", true));
                return;
            }
            if (name.length() > 20) {
                presenter.prepareFailView(new EditEventOutputData(eventId, "", "", "", true));
                return;
            }
            if (category != null && category.length() > 20) {
                presenter.prepareFailView(new EditEventOutputData(eventId, "", "", "", true));
                return;
            }
            if (description.length() > 100) {
                presenter.prepareFailView(new EditEventOutputData(eventId, "", "", "", true));
                return;
            }

            // ✅ Step 2: 查找原始事件
            Info event = dataAccess.getEventById(eventId);
            if (event == null) {
                presenter.prepareFailView(new EditEventOutputData(eventId, "", "", "", true));
                return;
            }

            // ✅ Step 3: 更新字段
            event.setName(name);
            event.setCategory(category);
            event.setDescription(description);

            // ✅ Step 4: 数据库写入
            boolean success = dataAccess.update(event);
            if (!success) {
                presenter.prepareFailView(new EditEventOutputData(eventId, name, category, description, true));
                return;
            }

            // ✅ Step 5: 返回成功视图
            EditEventOutputData outputData = new EditEventOutputData(
                    event.getId(),
                    event.getName(),
                    event.getCategory(),
                    event.getDescription(),
                    false
            );
            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            e.printStackTrace();
            presenter.prepareFailView(new EditEventOutputData(eventId, name, category, description, true));
        }
    }
}



