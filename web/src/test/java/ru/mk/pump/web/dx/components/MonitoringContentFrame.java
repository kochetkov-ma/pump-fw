package ru.mk.pump.web.dx.components;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.api.annotations.PElement;
import ru.mk.pump.web.component.BaseFrame;
import ru.mk.pump.web.elements.api.concrete.Button;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

public class MonitoringContentFrame extends BaseFrame {

    @Getter
    @PElement("Выбор Дашборда")
    @FindBy(id = "dashboardSelectorLink")
    private Button dashboardSelector;

    @Getter
    @FindBy(xpath = "//iframe[@id='dashboardFrame']")
    private FrameDashboard frame;

    //region CONSTRUCTORS
    public MonitoringContentFrame(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public MonitoringContentFrame(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public MonitoringContentFrame(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }
    //endregion

    public static class FrameDashboard extends BaseFrame {

        @Getter
        @FindBy(xpath = "//iframe[@id='WebResource_tasks_report']")
        private FrameReportOne frame;

        public FrameDashboard(By avatarBy, InternalElement parentElement) {
            super(avatarBy, parentElement);
        }
    }

    public static class FrameReportOne extends BaseFrame {

        @Getter
        @FindBy(xpath = "//iframe[@id='reportFrame']")
        private FrameReportTwo frame;

        public FrameReportOne(By avatarBy, InternalElement parentElement) {
            super(avatarBy, parentElement);
        }
    }

    public static class FrameReportTwo extends BaseFrame {

        @Getter
        @PElement("Поиск")
        @FindBy(xpath = "//span[@id='reportViewer_ReportViewer']/div/table//table//input[contains(@id,txtValue)]")
        private Input searchInput;

        public FrameReportTwo(By avatarBy, InternalElement parentElement) {
            super(avatarBy, parentElement);
        }
    }
}
