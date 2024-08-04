import React from 'react';
import APIResponseErrorMessage from "../commons/errorhandling/api-response-error-message.js";
import {
    Button,
    Card,
    CardHeader,
    Col,
    Modal,
    ModalBody,
    ModalHeader,
    Row
} from 'reactstrap';
import DeviceForm from "./components/device-form.js";
import * as API_DEVICE from "./api/device-api.js"
import DeviceTable from "./components/device-table.js";
import "./device-style.css";

class DeviceContainer extends React.Component {

    constructor(props) {
        super(props);
        this.toggleForm = this.toggleForm.bind(this);
        this.reload = this.reload.bind(this);
        this.state = {
            selected: false,
            collapseForm: false,
            tableData: [],
            isLoaded: false,
            errorStatus: 0,
            error: null,
            selectedDeviceData: null,
            isUpdate: false,
            token: sessionStorage.getItem('token'),
            selectedPersonId: sessionStorage.getItem('personId'),
            userRole: sessionStorage.getItem('personRole'),
            message: ''
        };
    }

    componentDidMount() {
        this.fetchDevices();
    }


    fetchDevices() {        
        const { token, userRole, selectedPersonId } = this.state;

        if (userRole === 'ADMIN') {
            return API_DEVICE.getDevices(token, (result, status, err) => {
                if (result !== null) {
                    this.setState({
                        tableData: result,
                        isLoaded: true
                    });
                } else {
                    this.setState(({
                        errorStatus: status,
                        error: err
                    }));
                }
            });
        } else if (userRole === 'CLIENT') {
            return API_DEVICE.getDevicesByPersonId(selectedPersonId, token, (result, status, err) => {
                if (result !== null) {
                    this.setState({
                        tableData: result,
                        isLoaded: true
                    });
                } else {
                    this.setState(({
                        errorStatus: status,
                        error: err
                    }));
                }
            });
        }
    }

    toggleForm() {
        this.setState({ selected: !this.state.selected });
    }

    reload() {
        this.setState({
            isLoaded: false,
            selectedDeviceData: null
        });
        this.toggleForm();
        this.fetchDevices();
    }

    deleteDevice = () => {
        const { token } = this.state;
        let id = sessionStorage.getItem('selectedPersonId');

        return API_DEVICE.deleteDeviceById(id, token, (result, status, err) => {
            if (status === 200 || status === 204) {
                this.setState({
                    isLoaded: false,
                    selectedDeviceData: null
                });
                this.fetchDevices();
            }
        });
    }

    handleUpdateDevice = () => {
        const selectedDeviceId = sessionStorage.getItem('selectedPersonId');
        const selectedDevice = this.state.tableData.find(device => device.id === selectedDeviceId);
        if (selectedDevice != null) {
            this.setState({ selectedDeviceData: selectedDevice, isUpdate: true });
            this.toggleForm();
        }
    }

    handleViewStatistics = () => {        
        this.props.history.push('/measurements');
    }



    render() {
        const { userRole } = this.state;

        return (
            <div>
                <div className="mainDiv">
                    <CardHeader  className="header">
                        <strong> Device Management </strong>
                    </CardHeader>
                    <Card className="card">
                        <br />
                        <Row>
                            <Col sm={{ size: '8', offset: 1 }}>
                                {userRole === 'ADMIN' && (
                                    <Button className="button" onClick={this.toggleForm}>
                                        Add Device 
                                    </Button>
                                )}
                                {userRole === 'ADMIN' && (
                                    <>
                                        <Button className="button" onClick={this.handleUpdateDevice}>
                                            Update Device 
                                        </Button>
                                        <Button className="button" onClick={this.deleteDevice}>
                                            Delete Device 
                                        </Button>
                                    </>
                                )}
                                {userRole === 'CLIENT' && (
                                    <Button className="button" onClick={this.handleViewStatistics}>
                                            View Statistics
                                    </Button>
                                )}
                            </Col>
                        </Row>
                        <br />
                        <Row>
                            <Col sm={{ size: '8', offset: 1 }}>
                                {this.state.isLoaded && <DeviceTable tableData={this.state.tableData} />}
                                {this.state.errorStatus > 0 && <APIResponseErrorMessage
                                    errorStatus={this.state.errorStatus}
                                    error={this.state.error}
                                />}
                            </Col>
                        </Row>
                    </Card>

                    <Modal isOpen={this.state.selected} toggle={this.toggleForm}
                        className={this.props.className} size="lg">
                        <ModalHeader toggle={this.toggleForm}> 
                        {this.state.isUpdate ? 'Update Device' : 'Add Device'}
                        </ModalHeader>
                        <ModalBody>
                            <DeviceForm 
                            reloadHandler={this.reload}
                            selectedDeviceData={this.state.selectedDeviceData}
                            />
                        </ModalBody>
                    </Modal>

                </div>
            </div>
        )

    }
}

export default DeviceContainer;
