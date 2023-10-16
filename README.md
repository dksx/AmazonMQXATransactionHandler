# AmazonMQXATransactionHandler

Executable jar for rolling back prepared transactions to recover an AmazonMQ broker, based on https://docs.aws.amazon.com/amazon-mq/latest/developer-guide/recover-xa-transactions.html

### Use

Shell into the Vidicore pod
```
cd /home/vidispine
```
```
curl --location https://github.com/dksx/AmazonMQXATransactionHandler/releases/download/1.0/AmazonMQTransactionHandler-1.0-jdk17-jar-with-dependencies.jar --output transactions.jar
```
```
java -jar transactions.jar {{brokerURL}}
```
where brokerURL is the Openwire endpoing, e.g `ssl://b-xxx-1.mq.eu-north-1.amazonaws.com:61617`
