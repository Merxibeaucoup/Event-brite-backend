package com.edgar.clone.eventbrite.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UseTicket {
	
	private long purchasedTicketid;
	private String eventName;

}
