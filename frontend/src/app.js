import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import NavigationBar from './navigation-bar.js';
import PersonContainer from './person/person-container.js';
import ErrorPage from './commons/errorhandling/error-page.js';
import styles from './commons/styles/project-style.css';
import Login from './authentication/login.js';
import DeviceContainer from './device/device-container.js';
import MeasurementsContainer from './measurements/measurements-container.js';
import SockJsClient from 'react-stomp';
import AdminChat from './chat/admin-chat.js';

const SOCKET_URL = 'http://localhost:8082/ws-endpoint';

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            alertMessage: null,
        };
    }

    onConnected = () => {
        console.log("Connected!!")
      }
    
     onMessageReceived = (msg) => {
        this.setState({ alertMessage: msg });
        alert(msg);
        console.log("Message received!! " + msg)
      }

      setAlertMessage = (message) => {
        this.setState({ alertMessage: message });
      }

    render() {
        const { alertMessage } = this.state;

        return (
            <div className={styles.back}>
                <SockJsClient url={SOCKET_URL} topics={['/topic/reply']}
                    onConnect={this.onConnected}
                    onDisconnect={console.log("Disconnected!")}
                    onMessage={this.onMessageReceived}
                    debug={true} />
                <Router>
                    <div>
                        <NavigationBar />
                        <Switch>
                            <Route exact path="/login" component={Login} />
                            <Route exact path="/person" component={PersonContainer} />

                            <Route exact path="/device" component={DeviceContainer} />
                            
                            <Route
                                exact
                                path="/measurements"
                                render={(props) => <MeasurementsContainer {...props} alertMessage={alertMessage} />}
                              />
                            <Route exact path = "/admin-chat" component={AdminChat} />
                            {/* Error */}
                            <Route exact path="/error" component={ErrorPage} />
                            <Route component={Login} />
                        </Switch>
                    </div>
                </Router>
            </div>
        );
    }
}

export default App;
