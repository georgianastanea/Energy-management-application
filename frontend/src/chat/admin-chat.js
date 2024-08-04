import React, { Component, useState } from "react";
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { HOST } from "../commons/hosts";
import { getConnectedPersons, getMessagesForPerson } from "./api/chat-api";
import './chat.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPaperPlane } from '@fortawesome/free-solid-svg-icons';


class AdminChat extends Component{

    constructor(props){
        super(props);

        this.state ={
            stompClient: null,
            connectedPersons: [],
            selectedPerson: null,
            chatMessages: [],
            messageInput: "",
            notifiedUsers: [],
            isTyping: false,
            seen: false,
        };
        this.connect();

    }

    connect = () => {
        const socket = new SockJS(HOST.chat_socket_url);
        this.stompClient = Stomp.over(socket);

        this.stompClient.connect({}, this.onConnected, this.onError);
    }

    onConnected = () => {
        const connectedUser = sessionStorage.getItem("username");
        const role = sessionStorage.getItem("personRole");
        this.stompClient.subscribe('/person/' + connectedUser + '/queue/messages',  this.onMessageReceived.bind(this));
        this.stompClient.subscribe('/person/' + connectedUser + '/queue/typing',  this.onTypingNotoficationReceived.bind(this));
        this.stompClient.subscribe('/person/' + connectedUser + '/queue/readReceipts',  this.onSeenNotoficationReceived.bind(this));

        this.stompClient.subscribe('/person/public', this.onMessageReceived.bind(this));

        // register the connected user
        this.stompClient.send("/app/person.addPerson",
            {},
            JSON.stringify({username: connectedUser, role: role})
        );

        // find and display the connected users
        this.findAndDisplayConnectedUsers();
    }

    findAndDisplayConnectedUsers () {
        const token = sessionStorage.getItem("token");
        const connectedUserRole = sessionStorage.getItem("personRole");
        getConnectedPersons(token, (result, status, err) => {
            if (result !== null && status === 200) {
              console.log(JSON.stringify(result));
              let connectedPersons = [];
              if(connectedUserRole === "CLIENT"){
                
                connectedPersons = result.filter(person => person.role === "ADMIN");
              }
              else if(connectedUserRole === "ADMIN"){
                connectedPersons = result.filter(person => person.username !== sessionStorage.getItem("username"));
              }
                this.setState({
                    connectedPersons: connectedPersons,
                });
            } else {
                this.setState({
                    errorStatus: status,
                    error: err
                });
            }
        });
        
    }
    onError = (err) => {
        console.log("Error!!")
    }

    updateChatMessages = (newMessage) => {
        this.setState(
          (prevState) => ({
            chatMessages: [...prevState.chatMessages, newMessage],
          }),
          () => {
            this.fetchMessagesForPerson(this.state.selectedPerson);
            this.findAndDisplayConnectedUsers(); 
          }
        );
      
        const notifiedUser = JSON.parse(newMessage.body).senderUsername;
      
        if (!this.state.notifiedUsers.includes(notifiedUser)) {
          this.setState((prevState) => ({
            notifiedUsers: [...prevState.notifiedUsers, notifiedUser],
          }));
        }
      };
      
    onTypingNotoficationReceived (msg) {
        if(this.state.selectedPerson === JSON.parse(msg.body).senderUsername) {
            this.setState({ isTyping: true, seen:true });
        }

        setTimeout(() => {
            this.setState({ isTyping: false });
          }, 1500);
    }

    onSeenNotoficationReceived (msg) {
        if(this.state.selectedPerson === JSON.parse(msg.body).senderUsername) {
            this.setState({ seen: true });
        }
    }

    onMessageReceived(msg) {
      if(this.state.selectedPerson === JSON.parse(msg.body).receiverUsername)
        this.setState({ seen: true });

        this.updateChatMessages(msg);
    }


    handleSelectPerson = (selectedPerson) => {
        this.setState({ selectedPerson: selectedPerson});
        this.setState({ seen: false});
        if(this.state.notifiedUsers.includes(selectedPerson)) {
            this.setState((prevState) => ({
                notifiedUsers: prevState.notifiedUsers.filter((user) => user !== selectedPerson),
                }));
        }
        this.fetchMessagesForPerson(selectedPerson);

        const message = {
            senderUsername: sessionStorage.getItem("username"),
            receiverUsername: selectedPerson,
            read: true
        };
        this.stompClient.send("/app/readReceipt", {}, JSON.stringify(message));

      }

    fetchMessagesForPerson = (selectedPerson) => {
          const token = sessionStorage.getItem("token");

        getMessagesForPerson(token, selectedPerson, (result, status, err) => {
            if (result !== null && status === 200) {
              // log last item in result
              console.log(result[result.length - 1]);
                this.setState({ chatMessages: result });
            } else {
                console.log("m am bulit")
            }
        })
    }

    renderConnectedPersons() {
        const { connectedPersons, selectedPerson, notifiedUsers } = this.state;

        return (
          <div className="connected-persons">
            <h2>Users</h2>
            <ul>
              {this.state.connectedPersons.map(person => (
                <li
                key={person.username}
                onClick={() => this.handleSelectPerson(person.username)}
                className={`${
                    person.username === selectedPerson
                      ? 'selected-user'
                      : notifiedUsers.includes(person.username)
                      ? 'notified-user'
                      : 'user'
                  }`}
                 >
                     {person.username}
                 </li>
              ))}
            </ul>
          </div>
        );
      }
    
      renderChatTab() {
        const { chatMessages, messageInput, selectedPerson, isTyping } = this.state;
        const connectedUser = sessionStorage.getItem("username");

        return (
          <div className="chat-tab">
            <h2>{selectedPerson}</h2>
            <div className="chat-messages">
              {chatMessages.map((message, index) => {
                const isSender = message.receiverUsername === connectedUser;
    
                return (
                  <div
                    key={index}
                    className={`chat-message ${isSender ? 'message-sender' : 'message-receiver'}`}
                  >
                    <span className="message-text">{message.content}</span>
                  </div>
                );
              })}
            </div>
            {isTyping && <div className="typing-indicator">Typing...</div>}
            {this.state.seen && <div className="seen-indicator">Seen</div>}
            <div className="message-input">
              <div className="input-container">
                <input
                  type="text"
                  placeholder="Type your message..."
                  value={messageInput}
                  onChange={this.handleInputChange}
                />
                <button className="send-button" onClick={this.handleSendMessage}>
                  <FontAwesomeIcon icon={faPaperPlane} />
                </button>
              </div>
            </div>
          </div>
        );
      }

      handleSendMessage = () => {
        const { messageInput, selectedPerson } = this.state;
      
        const message = {
          senderUsername: sessionStorage.getItem("username"),
          receiverUsername: selectedPerson,
          content: messageInput,
          timestamp: new Date()
        };
      
        this.stompClient.send("/app/chat", {}, JSON.stringify(message));
      
        this.setState({ messageInput: "",  seen: false });
        this.fetchMessagesForPerson(selectedPerson);
      };

      handleInputChange = (event) => {
        this.setState({ messageInput: event.target.value });

        const message = {
            senderUsername: sessionStorage.getItem("username"),
            receiverUsername: this.state.selectedPerson,
            typing: true
        };
        this.stompClient.send("/app/typing", {}, JSON.stringify(message));
      };


    
      render() {
        return (
          <div className="chat-container">
            {this.renderConnectedPersons()}
            {this.renderChatTab()}
          </div>
        );
      }
    };

export default AdminChat;