package com.ada.log.event;

import org.springframework.stereotype.Service;

import com.ada.log.event.base.AbstractPageEventHandle;
import com.ada.log.event.base.PageEventHandle;

@Service
public class MouseMoveEventHandle extends AbstractPageEventHandle implements PageEventHandle{

	public MouseMoveEventHandle() {
		super(new Integer[]{1,3,5,10}, "MouseMove");
	}
}
