/**
 * 
 */
package com.movitech.mbox.common.filter;

import com.movitech.mbox.common.utils.CacheUtils;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter;

/**
 * 页面高速缓存过滤器
 * @author ThinkGem
 * @version 2013-8-5
 */
public class PageCachingFilter extends SimplePageCachingFilter {

    @Override
    protected CacheManager getCacheManager() {
        return CacheUtils.getCacheManager();
    }
    
}
