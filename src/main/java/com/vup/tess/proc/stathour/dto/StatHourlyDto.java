package com.vup.tess.proc.stathour.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StatHourlyDto {
	private String statDateH;
	private String factoryId;
	private String pointId;
	private String energyGroupId;
	private BigDecimal pointValue;
	private BigDecimal pointAverage;
	private int pointCount;
	private LocalDateTime saveDate;
}
