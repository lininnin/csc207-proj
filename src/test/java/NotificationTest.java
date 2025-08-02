
import interface_adapter.Alex.Notification_related.*;
import use_case.Alex.Notification_related.*;
import view.Alex.NotificationView;

import java.time.LocalTime;

public class NotificationTest {

    public static void main(String[] args) {
        // Step 1: 创建 ViewModel 和 View（绑定监听器）
        NotificationViewModel viewModel = new NotificationViewModel();
        new NotificationView(viewModel);  // 注册监听器，准备弹窗

        // Step 2: 创建 Presenter
        NotificationPresenter presenter = new NotificationPresenter(viewModel);

        // Step 3: 模拟 DAO，返回固定时间点（例如 10:00）
        NotificationDataAccessObjectInterf fakeDAO = new NotificationDataAccessObjectInterf() {
            @Override
            public LocalTime getReminder1() {
                return LocalTime.of(10, 0);
            }

            @Override
            public LocalTime getReminder2() {
                return LocalTime.of(12, 0);
            }

            @Override
            public LocalTime getReminder3() {
                return LocalTime.of(20, 0);
            }
        };

        // Step 4: 创建 Interactor + Controller
        NotificationInteractor interactor = new NotificationInteractor(fakeDAO, presenter);
        NotificationController controller = new NotificationController(interactor);

        // Step 5: 模拟当前时间 = 10:00，执行触发器
        NotificationInputData inputData = new NotificationInputData(LocalTime.of(10, 0));
        interactor.execute(inputData);  // 直接调用 Interactor，也可以 controller.execute()
        //controller.execute();

        // 确认是否弹出提醒窗，内容应为：
        // "🧠 It's time to fill your wellness log!"
    }
}

