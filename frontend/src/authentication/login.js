import React from 'react';
import styles from './login.css';
import { performLogin } from './api/auth-api.js';
import validate from "../person/components/validators/person-validators.js";
import { withRouter } from 'react-router-dom';
import { getPersonByUsername } from '../person/api/person-api.js';



class Login extends React.Component {
    constructor(props) {
        super(props);
        this.toggleForm = this.toggleForm.bind(this);
        this.reloadHandler = this.props.reloadHandler;
        

        this.state = {
            errorStatus: 0,
            error: null,
            formIsValid: false,

            formControls: {
                username: {
                    value: '',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                        isRequired: true
                    }
                },
                password: {
                    value: '',
                    valid: false,
                    touched: false,
                    validationRules: {
                        passwordValidator: true
                    }
                }
            }
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    toggleForm() {
        this.setState({collapseForm: !this.state.collapseForm});
    }

    handleChange = event => {

        const username = event.target.name;
        const value = event.target.value;

        const updatedControls = this.state.formControls;

        const updatedFormElement = updatedControls[username];

        updatedFormElement.value = value;
        updatedFormElement.touched = true;
        updatedFormElement.valid = validate(value, updatedFormElement.validationRules);
        updatedControls[username] = updatedFormElement;

        let formIsValid = true;
        for (let updatedFormElementName in updatedControls) {
            formIsValid = updatedControls[updatedFormElementName].valid && formIsValid;
        }

        this.setState({
            formControls: updatedControls,
            formIsValid: formIsValid
        });

    };

    

    login(username, password) {
       
        return performLogin(username, password, (result, status, err) => {
            if (result !== null && (status === 200 || status === 201)) {
                sessionStorage.setItem('token', result.token);
                
                getPersonByUsername(username, result.token, (result, status, err) => {
                    if (result !== null && (status === 200 || status === 201)) {
                        sessionStorage.setItem("username", username);
                        sessionStorage.setItem('personId', result.id);
                        sessionStorage.setItem('personRole', result.role);

                        if(result.role === 'ADMIN')
                            this.props.history.push('/person');
                        else
                            this.props.history.push('/device');
                        
                    }
                });

              

            } else {
                this.setState(({
                    errorStatus: status,
                    error: err
                }));
            }
        });


    }

    handleSubmit() {
        let username = this.state.formControls.username.value
        let password =  this.state.formControls.password.value
        this.login(username, password);
    };

    handleRegisterClick = () => {
        this.props.history.push('/register');
    };

    render() {
        return (
            <div>
                <h1>Be in total control of your devices</h1>
                <form encType="application/x-www-form-urlencoded">
                    <div className="headingsContainer">
                        <h3>Sign in</h3>
                        <p>Sign in with your username and password</p>
                    </div>

                    <div className="mainContainer">
                        <label htmlFor="username">Your username</label>
                        <input type="text" name="username" placeholder="Enter Username" required  value={this.state.formControls.username.value} onChange={this.handleChange}/>

                        <br /><br />
                        <label htmlFor="pswrd">Your password</label>
                        <input type="password" name="password" placeholder="Enter Password" required value={this.state.formControls.password.value} onChange={this.handleChange}/>

                        <div className="subcontainer">
                            <label>
                                <input type="checkbox" name="remember" checked="checked" onChange={this.handleChange} /> Remember me
                            </label>
                            <p className="forgotpsd" onClick={this.handleRegisterClick}>Register</p>
                        </div>

                        <button type="button" onClick={this.handleSubmit}>Login</button>
                    </div>
                </form>
            </div>
        );
    }
}

export default withRouter(Login);