import React from 'react';
import validate from "./validators/device-validators.js";
import Button from "react-bootstrap/Button";
import * as API_CLIENTS from "../../person/api/person-api.js"
import * as API_DEVICE from "../api/device-api.js";
import APIResponseErrorMessage from "../../commons/errorhandling/api-response-error-message.js";
import { Col, Row } from "reactstrap";
import { FormGroup, Input, Label } from 'reactstrap';

class DeviceForm extends React.Component {

    isUpdate = !!this.props.selectedDeviceData;

    constructor(props) {
        super(props);
        this.toggleForm = this.toggleForm.bind(this);
        this.reloadHandler = this.props.reloadHandler;
        this.state = {
            errorStatus: 0,
            error: null,
            formIsValid: false,
            allClientList: [],

            formControls: {

                description: {
                    value: '',
                    placeholder: 'Give device description.',
                    valid: false,
                    touched: false,
                    validationRules: {
                        isRequired: true
                    }
                },

                address: {
                    value: '',
                    placeholder: 'Give device address.',
                    valid: false,
                    touched: false,
                    validationRules: {
                        isRequired: true
                    }
                },

                maxHourlyEnergyConsumption: {
                    value: '',
                    valid: false,
                    touched: false,
                    validationRules: {
                        isRequired: true
                    }
                },

                username: {
                    value: '',
                    placeholder: 'Choose user',
                    valid: false,
                    touched: false,
                    validationRules: {
                        isRequired: true
                    }
                }
            }
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);

    }

    toggleForm() {
        this.setState({ collapseForm: !this.state.collapseForm });
    }


    handleChange = (event) => {
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
            formIsValid: formIsValid,
        });
    };

    registerDevice(device) {
        let token = sessionStorage.getItem('token');
        return API_DEVICE.postDevice(device, token, (result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                this.reloadHandler();
            } else {
                if (status === 404) {
                    alert("Device already exists!");
                } else {
                    this.setState(({
                        errorStatus: status,
                        error: error
                    }));
                }
            }
        });
    }

    setInitialFormValues = () => {
        const { selectedDeviceData } = this.props;
        const { formControls } = this.state;

        if(this.isUpdate){
            formControls.description.value = selectedDeviceData.description || '';
            formControls.address.value = selectedDeviceData.address || '';
            formControls.maxHourlyEnergyConsumption.value = selectedDeviceData.maxHourlyEnergyConsumption || '';
            formControls.username.value = selectedDeviceData.username || '';

            formControls.description.valid = true;
            formControls.address.valid = true;
            formControls.maxHourlyEnergyConsumption.valid = true;
            formControls.username.valid = true;
        }
        else{
            formControls.description.value = '';
            formControls.address.value = '';
            formControls.maxHourlyEnergyConsumption.value = '';
            formControls.username.value = '';
        }
        this.setState({ formControls });
    };

    componentDidMount() {
        this.setInitialFormValues();
        this.fetchAllClients();
    };

    updateDevice() {
        let token = sessionStorage.getItem('token');
        let id = sessionStorage.getItem('selectedPersonId');
    
        API_CLIENTS.getPersonIdByUsername(this.state.formControls.username.value, token, (result, status, err) => {
            if (result !== null && status === 200) {
                let personId = result;
                let device = {
                    id: id,
                    description: this.state.formControls.description.value,
                    address: this.state.formControls.address.value,
                    maxHourlyEnergyConsumption: this.state.formControls.maxHourlyEnergyConsumption.value,
                    personId: personId
                };
    
                API_DEVICE.updateDevice(id, device, token, (result, status, err) => {
                    if (status === 200) {
                        this.toggleForm();
                        window.location.reload();

                    } else {
                        this.setState({
                            errorStatus: status,
                            error: err
                        });
                    }
                });
            } else {
                this.setState({ errorStatus: status, error: err });
            }
        });
    }
    



    handleSubmit() {
        const token = sessionStorage.getItem('token');
        const { username, description, address, maxHourlyEnergyConsumption } = this.state.formControls;
        
        API_CLIENTS.getPersonIdByUsername(username.value, token, (result, status, err) => {
            if (result !== null && status === 200) {
                const personId = result;
                
                const device = {
                    description: description.value,
                    address: address.value,
                    maxHourlyEnergyConsumption: maxHourlyEnergyConsumption.value,
                    personId: personId
                };
                
                if (this.isUpdate) {
                    this.updateDevice(device);
                } else {
                    this.registerDevice(device);
                }
            } else {
                this.setState({ errorStatus: status, error: err });
            }
        });
    }

    isFormValid = (formControls) => {
        for (const key in formControls) {
            if (!formControls[key].valid) {
                return false;
            }
        }
        return true;
    };

    fetchAllClients() {
        let token = sessionStorage.getItem('token');
        API_CLIENTS.getPersons(token, (result, status, err) => {
            if (result !== null && status === 200) {
                this.setState({ allClientList: result });
            } else {
                this.setState(({ errorStatus: status, error: err }));
            }
        });
    }

    render() {
        return (
            <div className='FormGroup'>
                <FormGroup id='description'>
                    <Label for='descriptionField'> Description: </Label>
                    <Input name='description' id='descriptionField' placeholder={this.state.formControls.description.placeholder}
                        onChange={this.handleChange}
                        defaultValue={this.state.formControls.description.value}
                        touched={this.state.formControls.description.touched ? 1 : 0}
                        valid={this.state.formControls.description.valid}
                        required
                    />
                    {this.state.formControls.description.touched && !this.state.formControls.description.valid &&
                        <div className={"error-message row"}> * Description must have at least 3 characters </div>}
                </FormGroup>

                <FormGroup id='address'>
                    <Label for='addressField'> Address: </Label>
                    <Input name='address' id='addressField' placeholder={this.state.formControls.address.placeholder}
                        onChange={this.handleChange}
                        defaultValue={this.state.formControls.address.value}
                        touched={this.state.formControls.address.touched ? 1 : 0}
                        valid={this.state.formControls.address.valid}
                        required
                    />
                    {this.state.formControls.address.touched && !this.state.formControls.address.valid &&
                        <div className={"error-message row"}> * Address must have at least 3 characters </div>}
                </FormGroup>

                <FormGroup id='maxHourlyEnergyConsumption'>
                    <Label for='maxHourlyEnergyConsumptionField'> Maximum hourly energy consumption: </Label>
                    <Input type="number" name='maxHourlyEnergyConsumption' id='maxHourlyEnergyConsumptionField' placeholder={this.state.formControls.maxHourlyEnergyConsumption.placeholder}
                        onChange={this.handleChange}
                        defaultValue={this.state.formControls.maxHourlyEnergyConsumption.value}
                        touched={this.state.formControls.maxHourlyEnergyConsumption.touched ? 1 : 0}
                        valid={this.state.formControls.maxHourlyEnergyConsumption.valid}
                        required
                    />
                    {this.state.formControls.maxHourlyEnergyConsumption.touched && !this.state.formControls.maxHourlyEnergyConsumption.valid &&
                        <div className={"error-message"}> * Maximum Value must have a valid format</div>}
                </FormGroup>

                <div>
                Client
                    <FormGroup>
                        <select name="username" id="username" className="form-select" onChange={this.handleChange}>
                            <option value="">Choose user</option> 
                            {this.state.allClientList.map((client, clientId) =>
                                <option value={client.username} key={clientId}>
                                    {client.username}
                                </option>
                            )}
                        </select>
                    </FormGroup>
                </div>

                <Row>
                    <Col sm={{ size: '4', offset: 8 }}>
                        <Button type={"submit"} disabled={!this.state.formIsValid} onClick={this.handleSubmit}>
                            {this.isUpdate ? "Update Device" : "Register Device"}
                        </Button>
                    </Col>
                </Row>

                {
                    this.state.errorStatus > 0 &&
                    <APIResponseErrorMessage errorStatus={this.state.errorStatus} error={this.state.error} />
                }
            </div>
        );
    }
}

export default DeviceForm;
