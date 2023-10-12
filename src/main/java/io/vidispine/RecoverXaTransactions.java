package io.vidispine;

import org.apache.activemq.ActiveMQXAConnectionFactory;

import javax.jms.XAConnection;
import javax.jms.XASession;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

public class RecoverXaTransactions {
    private static final String BROKER_USER_ENV_VAR = "MESSAGEBUS_USER";
    private static final String BROKER_PASS_ENV_VAR = "MESSAGEBUS_PASSWORD";
    private static ActiveMQXAConnectionFactory ActiveMQConnectionFactory;

    private static void InitConnection(String brokerUrl) {
        final String activeMqUsername = System.getenv(BROKER_USER_ENV_VAR);
        final String activeMqPassword = System.getenv(BROKER_PASS_ENV_VAR);
        ActiveMQConnectionFactory = new ActiveMQXAConnectionFactory(activeMqUsername, activeMqPassword, brokerUrl);
    }

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("Provide the Broker URL as the first argument");
                return;
            }

            InitConnection(args[0]);
            final XAConnection connection = ActiveMQConnectionFactory.createXAConnection();
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