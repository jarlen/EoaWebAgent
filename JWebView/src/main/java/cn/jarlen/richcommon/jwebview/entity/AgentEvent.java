package cn.jarlen.richcommon.jwebview.entity;

public enum AgentEvent {

    EVENT_PAGE_SHOW("onPageShow", "Activity恢复展示"),
    EVENT_PAGE_HIDE("onPageHide", "Activity暂停展示"),
    EVENT_PUSH_MSG_TO_PAGE("receiveMsg", "receive message from native"),
    EVENT_PAGE_RELEASE("onPageRelease", "Web page close");

    private String event;
    private String desc;

    AgentEvent(String eventName, String desc) {
        this.event = eventName;
        this.desc = desc;
    }

    public String getEvent() {
        return event;
    }

    public boolean equals(String event) {
        return this.event.equals(event);
    }
}
