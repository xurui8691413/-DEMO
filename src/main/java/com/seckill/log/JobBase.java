package com.seckill.log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public abstract class JobBase {

	 /// <summary>
    /// log4日志对象
    /// </summary>
   /* protected Logger getLogger(){
    	
    }
    {
        get
        {
            return log4net.LogManager.GetLogger(this.GetType());//得到当前类类型（当前实实例化的类为具体子类）
        }
    }*/
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
}
