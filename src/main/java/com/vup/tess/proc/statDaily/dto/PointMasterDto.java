package com.vup.tess.proc.statDaily.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data	// @Getter, @Setter, @ToString @ RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointMasterDto {
	private String factoryId;
	
	private String energyGroupId;
	
	private String pointId;

}
