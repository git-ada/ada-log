package com.ada.log.event;

import org.springframework.stereotype.Service;

import com.ada.log.event.base.AbstractPageEventHandle;
import com.ada.log.event.base.PageEventHandle;

@Service
public class MouseClickEventHandle extends AbstractPageEventHandle implements PageEventHandle{

	public MouseClickEventHandle() {
		super(new Integer[]{1,3,6,11}, "C");
	}
}
