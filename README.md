# SAR

## Présentation du projet :
Le projet se décompose en plusieurs parties dépendantes les une des autres.  
### Partie 1: Broker/Channel  

Spécifier, concevoir, implémenter et tester une couche de communication entre des "tâches", utile pour envoyer et recevoir des octets.  

### Partie 2: Queue

En s'appuyant sur le code de la partie 1, spécifier, concevoir, implémenter et tester une couche de communication entre des "tâches" au dessus du code de la partie 1 pour envoyer et recevoir des messages. Un message est un ensemble d'octets de taille variable et est envoyé et reçu dans son ensemble.

### Partie 3: Event  

Modifier la spécification et le code de la partie 2 pour passer en évenementiel plutôt qu'en multithreadé. (Le niveau de la partie 1 reste cependant en multithreadé)

## Avancement :
J'ai terminé la partie 1 et 2. Toutefois, je rencontre un problème d'interblocage des threads lorsque je lance mon broadcast que je n'avais pas détecté lors des précédents tests. Je n'ai pas encore réussie à résoudre le problème, je suppose qu'il y a un problème de synchronisation au niveau des read et write.  
J'ai commencé le design et la spec pour la partie 3 mais je n'ai pas eu le temps de finir.

## Organisation du travail et du code :
Je n'ai pas fait de tags car je suis encore en train de découvrir des bugs et donc encore en train de modifier les précédentes étapes.  
Mon code est organisé par packages.     
- La partie 1 possède 2 packages. Le package "channel" comprend le code des différentes classes qui permettent à deux tâches de communiquer entre elles (Broker, BrokerImpl, BrokerManager, Rdv, Channel, ChannelImpl, Task, CircularBuffer).    
Le package "channelTest" comprend un exemple d'utilisation de ce code par 2 tâches (TaskClient et TaskServer) ainsi qu'un test avec 1 serveur - 1 client (TestOneCLient) et un test 1 serveur - plusieurs clients (TestMultiCLients).    
- La partie 2 possède 3 packages. Le package "queue" correspond au code de la couche au dessus de la partie 1 pour permettre aux QueueBroker de se connecter et aux tâches de communiquer via les MessageQueue.(MessageQueue, MessageQueueImpl, QueueBroker, QueueBrokerImpl)  
Le package "testQueue" correspond au package "testChannel" mais pour le niveau des queue. (TaskQueueClient, TaskQueueServer, TestQueueOneClient, TestQueueMultiClient)  
Le package "testQueueBroadcast" comprends les classes nécessaire pour effectuer le test du breoadcast. (TaskClientReceiverBroadcast, TaskClientSenderBroadcast, TaskServerBroadcast, TestBroadcast)    

Le spécification finale est directement présente dans le code.
Les fichiers odt contiennent mes spécifications avant code, designs et note/correction faites en cours pour chaque partie. 
- Pour la partie 1: SARTD1_Broker comprend ma première spécification et design ainsi que différentes correction faite en cours.  
Dans ce design je prévoyais d'utiliser 2 circularBuffers, 1 pour les input et un 2e pour les output. Finalement chaque channel a son propre circularBuffer. 
Le fichier DesignBrokerCorrection correspond à la correction du design faite en en classe. C'est ce design que j'ai repris pour faire les connect et accept de la partie 1.   
- Pour la partie 2: SARTD3_QueueBroker comprend ma spécification avant code et mon design pour cette partie.
- Pour la partie 3: SARTD4_evenmentiel comprend une ébauche de spécification et de design pour le passage de la partie 2 en évenementiel.
- Le document SARTD5_Panne correspond au dernier cours fait sur les pannes et comprend mes notes (partie au stylo) et réponses aux questions (partie au crayon à papier)

## Comment le lancer : 
Si vous souhaitez tester la partie channel vous pouvez soit lancer la classe TestOneCLient soit la classe TestMultiCLients (présentes dans le package channelTest) selon le test que vous souhaitez effectuer.  
De même pour la partie queue vous pouvez soit lancer la classe TestQueueOneClient soit la classe TestQueueMultiClient (presentes dans le package testQueue). Pour lancer le test broadcast lancer la classe TestBroadcast du package testQueueBroadcast.  

