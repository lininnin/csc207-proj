package use_case.alex.WellnessLog_related.Moodlabel_related.edit_moodLabel;

import entity.Alex.MoodLabel.MoodLabelInterf;
import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelFactoryInterf;
import entity.Alex.MoodLabel.Type;

/**
 * Interactor for the EditMoodLabel use case.
 * Handles validation, data access, and presenter output.
 * Now fully decoupled from the concrete MoodLabel class by using the MoodLabelInterf interface.
 */
public class EditMoodLabelInteractor implements EditMoodLabelInputBoundary {

    private final EditMoodLabelDataAccessInterface dataAccess;
    private final EditMoodLabelOutputBoundary presenter;
    private final MoodLabelFactoryInterf factory;

    public EditMoodLabelInteractor(EditMoodLabelDataAccessInterface dataAccess,
                                   EditMoodLabelOutputBoundary presenter,
                                   MoodLabelFactoryInterf factory) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.factory = factory;
    }

    @Override
    public void execute(EditMoodLabelInputData inputData) {
        String originalName = inputData.getOriginalName();
        String newName = inputData.getNewName();
        String newType = inputData.getNewType();

        try {
            // ✅ Step 1: 输入合法性校验
            if (newName == null || newName.trim().isEmpty() || newName.length() > 20) {
                presenter.prepareFailView(new EditMoodLabelOutputData(newName, newType, true));
                return;
            }

            if (!"Positive".equalsIgnoreCase(newType) && !"Negative".equalsIgnoreCase(newType)) {
                presenter.prepareFailView(new EditMoodLabelOutputData(newName, newType, true));
                return;
            }

            // ✅ Step 2: 查找原始标签
            MoodLabelInterf originalLabel = dataAccess.getByName(originalName);
            if (originalLabel == null) {
                presenter.prepareFailView(new EditMoodLabelOutputData(newName, newType, true));
                return;
            }

            // ✅ Step 3: 构造新标签实体（用工厂而不是直接 new）
            Type type = "Positive".equalsIgnoreCase(newType)
                    ? Type.Positive
                    : Type.Negative;

            MoodLabelInterf updatedLabel = factory.create(newName, type);

            // ✅ Step 4: 数据库更新
            boolean success = dataAccess.update(updatedLabel);
            if (!success) {
                presenter.prepareFailView(new EditMoodLabelOutputData(newName, newType, true));
                return;
            }

            // ✅ Step 5: 成功视图
            EditMoodLabelOutputData outputData =
                    new EditMoodLabelOutputData(updatedLabel.getName(), newType, false);
            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            e.printStackTrace();
            presenter.prepareFailView(new EditMoodLabelOutputData(newName, newType, true));
        }
    }
}
