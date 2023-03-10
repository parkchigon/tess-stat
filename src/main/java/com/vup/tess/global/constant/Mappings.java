package com.vup.tess.global.constant;


import org.springframework.data.domain.Sort;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class Mappings {

	/***************************************************************************
	* URI PATH Mappings
	***************************************************************************/
	public static final String ROOT = "/";
	
	// API Mappings
	public static final String PREFIX_API = "/api";
	public static final String FACTORY_ADD = PREFIX_API + "/factory/set";
	public static final String FACTORY_FIND_LIST = PREFIX_API + "/factory/search/list";
	
	public static final String FACTORY_FIND_LIST_PAGE = PREFIX_API + "/factory/search/list/page";
	public static final int FACTORY_FIND_LIST_PAGE_size = 5;
	
	public static final String FACTORYGROUP_ADD = PREFIX_API + "/factorygroup/set";
	public static final String FACTORYGROUP_EDIT = PREFIX_API + "/factorygroup/edit";
	public static final String FACTORYGROUP_DEL = PREFIX_API + "/factorygroup/delete";

	
	// Web Mappings (admin)
	public static final String PREFIX_ADMIN = "/admin";
	public static final String FACTORY_GROUP_LIST = PREFIX_ADMIN + "/factorygroup/listall";
	
	public static final String FACTORY_GROUP_LIST_PAGE = PREFIX_ADMIN + "/factorygroup/list";
	public static final int FACTORY_GROUP_LIST_PAGE_size = 2;
	public static final String FACTORY_GROUP_LIST_PAGE_sort = "factoryGroupId";
	
	public static final String FACTORY_LIST_PAGE = PREFIX_ADMIN + "/factory/list";
	public static final int FACTORY_LIST_PAGE_size = 2;
	public static final String FACTORY_LIST_PAGE_sort = "factoryId";
	
	public static final String FACTORY_ENERGY_DETAIL = PREFIX_ADMIN + "/factoryenergy/{factoryid}/{energygroupid}";
	
	// Web Mappings (user)
	public static final String PREFIX_USER = "/user";
	
	
	/***************************************************************************
	* API id Mappings
	***************************************************************************/
	public final static String API_ID_FACTORYGROUP_ADD = "FACTORYGROUP_ADD";
	public final static String API_ID_FACTORYGROUP_EDIT = "FACTORYGROUP_EDIT";
	public final static String API_ID_FACTORYGROUP_DELETE = "FACTORYGROUP_DELETE";
	public final static String API_ID_FACTORY_ADD = "FACTORY_ADD";
	public final static String API_ID_FACTORY_DELETE = "FACTORY_DELETE";
	public final static String API_ID_FACTORY_SEARCH = "FACTORY_SEARCH";
	public final static String API_ID_FACTORY_SEARCH_PGAE = "FACTORY_SEARCH_PAGE";

	
}
