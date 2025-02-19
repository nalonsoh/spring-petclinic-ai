/**
 * @description TS that contains component with chat
 */
import { Component } from '@angular/core';
import { marked } from 'marked';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';

/**
 * @description Component that paints the chat
 */
@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent {
  protected baseUrl = environment.REST_API_URL;

  /**
   * @description Constructor
   */
  constructor(private readonly http: HttpClient) {
    // this is intentional
  }

  toggleChatbox() {
    const chatbox = document.getElementById('chatbox');
    const chatboxContent = document.getElementById('chatbox-content');

    console.log(chatbox)
    if (chatbox && chatboxContent) {

      if (chatbox.classList.contains('minimized')) {
        chatbox.classList.remove('minimized');
        chatboxContent.style.height = '400px'; // Set to initial height when expanded
      } else {
        chatbox.classList.add('minimized');
        chatboxContent.style.height = '40px'; // Set to minimized height
      }
    }
  }

  sendMessage() {
    const input = <HTMLInputElement>document.getElementById('chatbox-input');
    const check = <HTMLInputElement>document.getElementById('useMemory-check');
    const modeSelect = <HTMLInputElement>document.getElementById('mode-selector');
    if (!input ||!check) return;

    // Only send if there's a message
    if (!input.value.trim()) return;

    let useChatMemory = "false";
    if (check.checked) {
      useChatMemory = "true";
    }
    
    const query = {
      'message': input.value,
      'useChatMemory': useChatMemory,
      'cid': this.getChatCid(),
      'mode': modeSelect.value
    };
    
    // Display user message in the chatbox
    this.appendMessage(input.value, 'user');
    
    // Clear the input field after sending the message
    input.value = '';
    
    console.log(" => User prompt:" + JSON.stringify(query))
    // Send the message to the backend
    this.http.post(this.baseUrl + 'ai/chat', query).subscribe({
      next: response => {
         // Display the response from the server in the chatbox
         console.log("Respuesta: " + response);
         const htmlContent = <string> marked.parse((<any>response).completion);
 
         this.appendMessage(htmlContent, 'bot');
      },
      error: error => {
        console.log(error);
      }
   });
  }

  appendMessage(message: string, type: string) {
    const chatMessages = document.getElementById('chatbox-messages');
    if (!chatMessages) return;

    const messageElement = document.createElement('div');
    messageElement.classList.add('chat-bubble', type);

    // Convert Markdown to HTML
    //const htmlContent = marked.parse(message); // Use marked.parse() for newer versions
    const htmlContent = message;
    messageElement.innerHTML = htmlContent;

    chatMessages.appendChild(messageElement);

    // Scroll to the bottom of the chatbox to show the latest message
    chatMessages.scrollTop = chatMessages.scrollHeight;
  }

  cleanChat() {
    this.deleteChatCid();

    const chatMessages = document.getElementById('chatbox-messages');
    if (!chatMessages) return;

    chatMessages.innerHTML = '';
  }

  keyPress(event: KeyboardEvent) {
    
    let sendOneEnter = true;
    const check = <HTMLInputElement>document.getElementById('sendOnEnter-check');
    if (!check.checked) {
      sendOneEnter = false;
    }

    if (sendOneEnter && event.key == "Enter") {
      event.preventDefault();
      this.sendMessage();
    }
  }

  getChatCid(): string {
    let miStorage = window.localStorage;
    let chatCid = miStorage.getItem("chatCid");

    if (null === chatCid) {
      chatCid = this.createRandomString(15);
      miStorage.setItem("chatCid", chatCid)
    }

    console.log("ChatCid: " + chatCid);
    return chatCid;
  }

  deleteChatCid(): void {
    let miStorage = window.localStorage;
    miStorage.removeItem("chatCid");
  }


  createRandomString(length: number): string {
    const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    let result = "";
    for (let i = 0; i < length; i++) {
      result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
  }
  
}
