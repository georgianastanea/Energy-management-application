import React from 'react';
import validate from "./validators/person-validators.js";
import Button from "react-bootstrap/Button";
import * as API_USERS from "../api/person-api.js";
import APIResponseErrorMessage from "../../commons/errorhandling/api-response-error-message.js";
import {Col, Row} from "reactstrap";
import { FormGroup, Input, Label} from 'reactstrap';
import Select from 'react-select';



class PersonForm extends React.Component {

    isUpdate = !!this.props.selectedPersonData;

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
                    placeholder: 'What is the name of the client?...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                        isRequired: true
                    }
                },
                password: {
                    value: '',
                    placeholder: 'Password...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        passwordValidation: true
                    }
                },
                role: {
                    value: '',
                    placeholder: 'Role...',
                    valid: false,
                    touched: false,
                },
            }
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    toggleForm() {
        this.setState({collapseForm: !this.state.collapseForm});
    }


    handleChange = event => {

        const name = event.target.name;
        const value = event.target.value;

        const updatedControls = this.state.formControls;

        const updatedFormElement = updatedControls[name];

        updatedFormElement.value = value;
        updatedFormElement.touched = true;
        updatedFormElement.valid = validate(value, updatedFormElement.validationRules);
        updatedControls[name] = updatedFormElement;

        let formIsValid = true;
        for (let updatedFormElementName in updatedControls) {
            formIsValid = updatedControls[updatedFormElementName].valid && formIsValid;
        }

        this.setState({
            formControls: updatedControls,
            formIsValid: formIsValid
        });

    };

    registerPerson(person) {
        return API_USERS.postPerson(person, (result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                this.reloadHandler();
            } else {
                this.setState(({
                    errorStatus: status,
                    error: error
                }));
            }
        });
    }

    handleSubmit() {
        let person = {
            username: this.state.formControls.username.value,
            password: this.state.formControls.password.value,
            role: this.state.formControls.role.value,
        };
       
        if(this.isUpdate)
            this.updatePerson();
        else
            this.registerPerson(person);
        console.log(person);
    }

    handleRoleChange = (selectedOption) => {
        const updatedControls = this.state.formControls;
        updatedControls.role.value = selectedOption.value;
        updatedControls.role.touched = true;

        updatedControls.role.valid = selectedOption.value !== '';
        this.setState({
            formControls: updatedControls,
            formIsValid: this.isFormValid(updatedControls),
        });
    };

    isFormValid = (formControls) => {
        for (const key in formControls) {
            if (!formControls[key].valid) {
                return false;
            }
        }
        return true;
    };

    updatePerson(){
        let token = sessionStorage.getItem('token');
        let id = sessionStorage.getItem('selectedPersonId');

        let person = {
            username: this.state.formControls.username.value,
            password: this.state.formControls.password.value,
            role: this.state.formControls.role.value,
        };

        return API_USERS.updatePerson(id, person, token, (result, status, err) => {
            console.log("status from backed update " + result + " " + status + " " + err);
            if (status === 200) {
                this.reloadHandler();
            } else {
                this.setState({
                    errorStatus: status,
                    error: err
                });
            }
        });
    }

    componentDidMount() {
        this.setInitialFormValues();
    }

    setInitialFormValues = () => {
        const { selectedPersonData } = this.props;
        const { formControls } = this.state;

        if(this.isUpdate){
            formControls.username.value = selectedPersonData.username || '';
            formControls.role.value = selectedPersonData.role || '';
            formControls.username.valid = true;
            formControls.role.valid = true;
        }
        else{
            formControls.username.value = '';
        }
        this.setState({ formControls });
    };




    render() {

        const roleOptions = [
            { value: 'ADMIN', label: 'ADMIN' },
            { value: 'CLIENT', label: 'CLIENT' },
        ];

        return (
            <div className="FormGroup">

                <FormGroup id='username'>
                    <Label for='usernameField'> Username: </Label>
                    <Input name='username' id='nameField' placeholder={this.state.formControls.username.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.username.value}
                           touched={this.state.formControls.username.touched? 1 : 0}
                           valid={this.state.formControls.username.valid}
                           required
                    />
                    {this.state.formControls.username.touched && !this.state.formControls.username.valid &&
                    <div className={"error-message row"}> * Username must have at least 3 characters </div>}
                </FormGroup>

                <FormGroup id='password'>
                    <Label for='password'> Password: </Label>
                    <Input name='password' id='passwordField' placeholder={this.state.formControls.password.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.password.value}
                           touched={this.state.formControls.password.touched? 1 : 0}
                           valid={this.state.formControls.password.valid}
                           required
                    />
                    {this.state.formControls.password.touched && !this.state.formControls.password.valid &&
                    <div className={"error-message"}> * Password must have at least 3 characters</div>}
                </FormGroup>

                 <FormGroup id="role">
                    <Label for="roleField"> Role: </Label>
                    <Select
                        name="role"
                        id="roleField"
                        options={roleOptions}
                        value={roleOptions.find((option) => option.value === this.state.formControls.role.value)}
                        onChange={this.handleRoleChange}
                    />
                    {this.state.formControls.role.touched && !this.state.formControls.role.valid && (
                        <div className="error-message row"> * Please select a role</div>
                    )}
                </FormGroup>

                <Row>
                    <Col sm={{ size: '4', offset: 8 }}>
                        <Button type="submit" disabled={!this.state.formIsValid} onClick={this.handleSubmit}>
                            {this.isUpdate ? "Update" : "Submit"}
                        </Button>
                    </Col>
                </Row>

                {
                    this.state.errorStatus > 0 &&
                    <APIResponseErrorMessage errorStatus={this.state.errorStatus} error={this.state.error}/>
                }
            </div>
        ) ;
    }
}

export default PersonForm;
