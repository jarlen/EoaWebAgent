package cn.jarlen.richcommon.jwebview.entity;

public enum AgentEvent {

    EVENT_ACTIVITY_RESUME("onResume", "Activity恢复展示"),
    EVENT_ACTIVITY_PAUSE("onPause", "Activity暂停展示"),
    EVENT_WEB_CLOSE("onWebClose", "Web page close");

    private String eventName;
    private String eventDesc;

    AgentEvent(String eventName, String eventDesc) {
        this.eventName = eventName;
        this.eventDesc = eventDesc;
    }

    public String getEventName() {
        return eventName;
    }
}
