import authentication.SymBotAuth;
import clients.SymBotClient;
import configuration.SymConfig;
import configuration.SymConfigLoader;
import listeners.IMListener;
import listeners.RoomListener;
import model.OutboundMessage;
import model.UserInfo;
import services.DatafeedEventsService;

import javax.ws.rs.core.NoContentException;
import java.net.URL;

public class BotExample {

    public static void main(String [] args) {
        BotExample app = new BotExample();
    }

    public BotExample() {
        URL url = getClass().getResource("config.json");
        SymConfigLoader configLoader = new SymConfigLoader();
        SymConfig config = configLoader.loadFromFile(url.getPath());
        SymBotAuth botAuth = new SymBotAuth(config);
        botAuth.authenticate();
        SymBotClient botClient = SymBotClient.initBot(config, botAuth);
        DatafeedEventsService datafeedEventsService = botClient.getDatafeedEventsService();
        RoomListener roomListenerTest = new RoomListenerTestImpl(botClient);
        datafeedEventsService.addRoomListener(roomListenerTest);
        IMListener imListener = new IMListenerImpl(botClient);
        datafeedEventsService.addIMListener(imListener);
        createRoom(botClient);
    }

    private void createRoom(SymBotClient botClient){
        try {
            UserInfo userInfo = botClient.getUsersClient().getUserFromEmail("commandercheng@gmail.com", true);
            //get user IM and send message
            String IMStreamId = botClient.getStreamsClient().getUserIMStreamId(userInfo.getId());
            OutboundMessage message = new OutboundMessage();
            message.setMessage("test IM");
            botClient.getMessagesClient().sendMessage(IMStreamId,message);

//            Room room = new Room();
//            room.setName("octopus horse octopus horse horse " + UUID.randomUUID().toString());
//            room.setDescription("test");
//            room.setDiscoverable(true);
//            room.setPublic(true);
//            room.setViewHistory(true);
//
//            RoomInfo roomInfo = botClient.getStreamsClient().createRoom(room);
//            botClient.getStreamsClient().addMemberToRoom(roomInfo.getRoomSystemInfo().getId(),userInfo.getId());
//
//            Room newRoomInfo = new Room();
//            newRoomInfo.setName("test generator " + UUID.randomUUID().toString());
//            botClient.getStreamsClient().updateRoom(roomInfo.getRoomSystemInfo().getId(),newRoomInfo);
//
//            List<RoomMember> members =  botClient.getStreamsClient().getRoomMembers(roomInfo.getRoomSystemInfo().getId());
//
//            botClient.getStreamsClient().promoteUserToOwner(roomInfo.getRoomSystemInfo().getId(), userInfo.getId());
//
//            botClient.getStreamsClient().deactivateRoom(roomInfo.getRoomSystemInfo().getId());
        } catch (NoContentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
