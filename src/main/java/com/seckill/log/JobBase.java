package com.seckill.log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public abstract class JobBase {

	 /// <summary>
    /// log4��־����
    /// </summary>
   /* protected Logger getLogger(){
    	
    }
    {
        get
        {
            return log4net.LogManager.GetLogger(this.GetType());//�õ���ǰ�����ͣ���ǰʵʵ��������Ϊ�������ࣩ
        }
    }*/
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
}
