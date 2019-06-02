    //Arguments from the command line provide
    //the port number to bind the socket.
    public static void main(String[] args) {
        parseAndVerifyArguments();

        //Create the instance, socket and initializes the 
        //buffer and packet.
        Receiver receiverObject = new Receiver();

        //Random number to start the sequence to sending
        //the message.
        double previousSequenceNumber;
        double currentSequenceNumber;

        //The client will receive the length of the 
        //message, size of each packet and starting
        //sequence number.
        receiverObject.receive();
        previousSequenceNumber = receiverObject.getSequenceNumber();
        int packetSize = receiverObject.getPacketLength();
        int messageSize = receiverObject.getMessage().length();
        int numOfPacketsExpected = messageSize / packetSize;

        //Estimate the number of iterations needed to receive the message.
        if (numOfPacketsExpected < 1) {
            //Receives the entire packet in one iteration.
            receiverObject.receive();
        } else {
            //Receives the packets in multiple iterations and stores
            //the missed packets. 
            numOfPacketsExpected = Math.ceil(numOfPacketsExpected);
            for (int index = 0; index < numOfPacketsExpected; index++) {
                receiverObject.receive();
                currentSequenceNumber = receiverObject.getSequenceNumber();
                if (currentSequenceNumber != previousSequenceNumber + 1) {
                    receiverObject.getMissedPacketsMap().add(receiverObject.getServerAddress(), currentSequenceNumber-1);
                }

                previousSequenceNumber = currentSequenceNumber;
            }
        }

        //Once all the packets are received, the receiver checks for any missed
        //packets from the senders, and makes a request for retransmission.
    }