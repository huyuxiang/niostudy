package rocketmq;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;

public class Consumer {
	public static void main(String args[]) {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("PushConsumer");
		consumer.setNamesrvAddr("10.100.142.65:9876");
		try {
			consumer.subscribe("PullTopic", "pull");

			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
			consumer.registerMessageListener(new MessageListenerConcurrently() {
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
					Message msg = list.get(0);
					System.out.println(new String(msg.getBody()));
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			});
			consumer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
