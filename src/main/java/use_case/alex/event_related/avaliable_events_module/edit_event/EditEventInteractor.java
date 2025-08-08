package use_case.alex.event_related.avaliable_events_module.edit_event;

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
                presenter.prepareFailView(new EditEventOutputData(eventId, "", "", "Event name cannot be empty.", true));
                return;
            }

            if (name.length() > 20) {
                presenter.prepareFailView(new EditEventOutputData(eventId, "", "", "Event name too long (max 20 characters).", true));
                return;
            }

            if (category != null && category.length() > 20) {
                presenter.prepareFailView(new EditEventOutputData(eventId, "", "", "Category too long (max 20 characters).", true));
                return;
            }

            if (description.length() > 100) {
                presenter.prepareFailView(new EditEventOutputData(eventId, "", "", "Description too long (max 100 characters).", true));
                return;
            }


            // ✅ Step 2: 查找原始事件
            Info event = dataAccess.getEventById(eventId);
            if (event == null) {
                presenter.prepareFailView(new EditEventOutputData(eventId, "", "", "Original event not found.", true));
                return;
            }

// ✅ Step 3: 冲突校验（插入于 setName 之前）
            if (!name.equals(event.getName())) {
                for (Info other : dataAccess.getAllEvents()) {
                    if (!other.getId().equals(eventId) && name.equals(other.getName())) {
                        presenter.prepareFailView(new EditEventOutputData(eventId, "", "", "Event name already exists.", true));
                        return;
                    }
                }
            }


// ✅ Step 4: 更新字段
            event.setName(name);
            event.setCategory(category);
            event.setDescription(description);


            // ✅ Step 4: 数据库写入
            boolean success = dataAccess.update(event);
            if (!success) {
                presenter.prepareFailView(new EditEventOutputData(eventId, name, category, "Database update failed.", true));
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
            presenter.prepareFailView(new EditEventOutputData(eventId, name, category, "Unexpected error: " + e.getMessage(), true));
        }

    }
}
