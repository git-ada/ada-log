package com.ada.log.event;

import org.springframework.stereotype.Service;

import com.ada.log.event.base.AbstractPageEventHandle;
import com.ada.log.event.base.PageEventHandle;

@Service
public class ScrollEventHandle extends AbstractPageEventHandle implements PageEventHandle{

	public ScrollEventHandle() {
		super(new Integer[]{1,3,5,10}, "Scroll");
	}
}
