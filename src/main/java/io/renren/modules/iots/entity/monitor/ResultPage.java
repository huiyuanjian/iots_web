package io.renren.modules.iots.entity.monitor;

import java.util.List;

public class ResultPage {

    private OurPage page;

    private List<IoserverDto> list;

    public OurPage getPage() {
        return page;
    }

    public void setPage(OurPage page) {
        this.page = page;
    }

    public List<IoserverDto> getList() {
        return list;
    }

    public void setList(List<IoserverDto> list) {
        this.list = list;
    }
}
