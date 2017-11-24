package com.ada.log.event;

import org.springframework.stereotype.Service;

import com.ada.log.event.base.AbstractPageEventHandle;
import com.ada.log.event.base.PageEventHandle;

@Service
public class StayTimeEventHandle extends AbstractPageEventHandle implements PageEventHandle{

	public StayTimeEventHandle() {
		super(new Integer[]{5,30,120,300}, "StayTime");
	}
}
