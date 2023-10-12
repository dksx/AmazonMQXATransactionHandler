package io.vidispine;

import org.apache.activemq.ActiveMQXAConnectionFactory;

import javax.jms.XAConnection;
import javax.jms.XASession;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

public class RecoverXaTransactions {
    private static final String BrokerUserEnv = "MESSAGEBUS_USER";
    private static final String BrokerPassEnv = "MESSAGEBUS_PASSWORD";
    private static ActiveMQXAConnectionFactory ACTIVE_MQ_CONNECTION_FACTORY;

    private static void InitConnection(String brokerUrl) {
        final String activeMqUsername = System.getenv(BrokerUserEnv);
        final String activeMqPassword = System.getenv(BrokerPassEnv);
        ACTIVE_MQ_CONNECTION_FACTORY = new ActiveMQXAConnectionFactory(activeMqUsername, activeMqPassword, brokerUrl);
        ACTIVE_MQ_CONNECTION_FACTORY.setUserName(activeMqUsername);
        ACTIVE_MQ_CONNECTION_FACTORY.setPassword(activeMqPassword);
    }

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("Provide the Broker URL as the first argument");
                return;
            }

            InitConnection(args[0]);
            final XAConnection connection = ACTIVE_MQ_CONNECTION_FACTORY.createXAConnection();
            XASession xaSession = connection.createXASession();
            XAResource xaRes = xaSession.getXAResource();

            for (Xid id : xaRes.recover(XAResource.TMENDRSCAN)) {
                System.out.println("Rolling back transaction with ID " + id);
                xaRes.rollback(id);
            }
            connection.close();

        } catch (Exception e) {
            System.out.println("Shit happened - " + e.getMessage());
        }
    }
}