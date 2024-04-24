package org.example.app.client;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Scanner;

public class UserClientHandler extends SimpleChannelInboundHandler<String> {

    private final String username;

    public UserClientHandler(String username) {
        this.username = username;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        if (msg.equalsIgnoreCase("exit")) {
            System.out.println("Server closed the connection.");
            ctx.close();
        } else {
            System.out.println("SERVER: " + msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Connected to server. Type 'quit' to exit.");
        startSendingMessages(ctx);
    }

    private void startSendingMessages(ChannelHandlerContext ctx) {
        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            System.out.print("Enter message: ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("quit")) {
                ctx.writeAndFlush(username + " left the chat");
                ctx.close();
                System.exit(0);
                break;
            }
            ctx.writeAndFlush(username + ": " + input);
        }
        scanner.close();
    }
}
