import React from 'react';
import APIResponseErrorMessage from '../commons/errorhandling/api-response-error-message.js';
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
import PersonForm from './components/person-form.js';
import * as API_USERS from './api/person-api.js';
import PersonTable from './components/person-table.js';
import './person-container.css'; 

class PersonContainer extends React.Component {
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
            selectedPersonData: null,
            isUpdate: false
        };
    }

    componentDidMount() {
        this.fetchPersons();
    }

    fetchPersons() {
        let token = sessionStorage.getItem('token');
        return API_USERS.getPersons(token, (result, status, err) => {
            if (result !== null && status === 200) {
                this.setState({
                    tableData: result,
                    isLoaded: true
                });
            } else {
                this.setState({
                    errorStatus: status,
                    error: err
                });
            }
        });
    }

    toggleForm() {
        this.setState({ selected: !this.state.selected });
    }

    reload() {
        this.setState({
            isLoaded: false,
            selectedPersonData: null
        });
        this.toggleForm();
        this.fetchPersons();
    }

    deletePerson = () =>{
        let token = sessionStorage.getItem('token');
        let id = sessionStorage.getItem('selectedPersonId');

        return API_USERS.deletePerson(id, token, (result, status, err) => {
            if (status === 200 || status === 204) {
                this.setState({
                    isLoaded: false,
                    selectedPersonData: null
                });
                this.fetchPersons();
            }            
        });
    }

    handleUpdatePerson = () => {
        const selectedPersonId = sessionStorage.getItem('selectedPersonId');
        const selectedPerson = this.state.tableData.find((person) => person.id === selectedPersonId);
        if (selectedPerson != null) {
            this.setState({ selectedPersonData: selectedPerson, isUpdate: true });
            this.toggleForm();
        }
    };

    

    render() {
        return (
            <div className='mainDiv'>
                <CardHeader className="header">
                    <strong>Person Management</strong>
                </CardHeader>
                <Card className="card">
                    <Row>
                        <Col sm={{ size: '8', offset: 1 }}>
                            <Button className="button" onClick={this.toggleForm}>
                                Add Person
                            </Button>
                            <Button className="button" onClick={this.handleUpdatePerson}>
                                Update Person
                            </Button>
                            <Button className="button" onClick={this.deletePerson}>
                                Delete Person
                            </Button>
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={{ size: '8', offset: 1 }}>
                            {this.state.isLoaded && <PersonTable tableData={this.state.tableData} />}
                            {this.state.errorStatus > 0 && (
                                <APIResponseErrorMessage
                                    errorStatus={this.state.errorStatus}
                                    error={this.state.error}
                                />
                            )}
                        </Col>
                    </Row>
                </Card>
                <Modal isOpen={this.state.selected} toggle={this.toggleForm} className={this.props.className} size="lg">
                    <ModalHeader toggle={this.toggleForm}>
                        {this.state.isUpdate ? 'Update Person' : 'Add Person'}
                    </ModalHeader>
                    <ModalBody>
                        <PersonForm
                        reloadHandler={this.reload}
                        selectedPersonData={this.state.selectedPersonData}
                        />
                    </ModalBody>
                </Modal>
            </div>
        );
    }
}

export default PersonContainer;
