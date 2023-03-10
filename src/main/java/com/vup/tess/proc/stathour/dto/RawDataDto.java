package com.vup.tess.proc.stathour.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import com.vup.tess.model.PointMaster;
import com.vup.tess.model.RawData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data	// @Getter, @Setter, @ToString @ RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawDataDto {
	

	//private long seq;
	
	//private String factoryId;
	
	//private String energyGroupId;
	
	//private String pointId;
		
	private String pointStatusDate;
	
	private String pointStatusTime;
	
	//private BigDecimal pointValue;
	
	//private BigDecimal pointChangeValue;
	
	//private LocalDateTime saveDate;
	
	
}
