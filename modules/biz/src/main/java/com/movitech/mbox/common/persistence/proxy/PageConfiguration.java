/**
 * 
 */
package com.movitech.mbox.common.persistence.proxy;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * <p>
 * 自定义Mybatis的配置，扩展.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2012-05-13 上午10:06
 * @since JDK 1.5
 */
public class PageConfiguration extends Configuration {
    
    protected MapperRegistry subMapperRegistry = new PaginationMapperRegistry(this);

    @Override
    public <T> void addMapper(Class<T> type) {
    	subMapperRegistry.addMapper(type);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return subMapperRegistry.getMapper(type, sqlSession);
    }

    @Override
    public boolean hasMapper(Class<?> type) {
        return subMapperRegistry.hasMapper(type);
    }
    
    @Override
    public void addMappers(String packageName, Class<?> superType) {
    	subMapperRegistry.addMappers(packageName, superType);
    }

    @Override
    public void addMappers(String packageName) {
    	subMapperRegistry.addMappers(packageName);
    }
    
    @Override
    public MapperRegistry getMapperRegistry() {
        return subMapperRegistry;
    }

}
