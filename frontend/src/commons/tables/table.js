import React, {Component} from "react";
import ReactTable from 'react-table';
import 'react-table/react-table.css';
import Field from "./fields/Field";
import {Col, Row} from "react-bootstrap";
import './table.css';


class Table extends Component {
    constructor(props) {
        super(props);

        this.state = {
            data: props.data,
            columns: props.columns,
            search: props.search,
            filters: [],
            getTrPropsFunction: props.getTrProps,
            pageSize: props.pageSize || 10,
            selectedPersonId: null,
        };
    }

    search() {

    }

    filter(data) {
        let accepted = true;

        this.state.filters.forEach(val => {
            if (String(val.value) === "") {
                accepted = true;
            }

            if (!String(data[val.accessor]).includes(String(val.value)) && !String(val.value).includes(String(data[val.accessor]))) {
                accepted = false;
            }
        });

        return accepted;
    }

    handleChange(value, index, header) {
        if (this.state.filters === undefined)
            this.setState({filters: []});

        this.state.filters[index] = {
            value: value.target.value,
            accessor: header
        };

        this.forceUpdate();
    }


    getTRPropsType(state, rowInfo) {
        if (rowInfo) {
            return {
                style: {
                    textAlign: "center",
                    cursor: "pointer", 
                },
            };
        } else {
            return {};
        }
    }


    onRowClick = (state, rowInfo) => {
        return {
            onClick: (e, handleOriginal) => {
                if (handleOriginal) {
                    handleOriginal();
                }
                if (rowInfo && rowInfo.original) {
                    this.setState({ selectedPersonId: rowInfo.original.id });
                    sessionStorage.setItem('selectedPersonId', rowInfo.original.id);
                }
            },
            style: {
                background: rowInfo && rowInfo.original && rowInfo.original.id === this.state.selectedPersonId ? '#e0e0e0' : 'white',
            },
        };
    }

    render() {
        let data = this.state.data ? this.state.data.filter(data => this.filter(data)) : [];

        return (
            <div>
                <Row>
                    {
                        this.state.search.map((header, index) => {
                            return (
                                <Col key={index}>
                                    <div >
                                        <Field id={header.accessor} label={header.accessor}
                                               onChange={(e) => this.handleChange(e, index, header.accessor)}/>
                                    </div>
                                </Col>
                            )
                        })
                    }
                </Row>
                <Row>
                    <Col>
                        <ReactTable
                            data={data}
                            resolveData={data => data.map(row => row)}
                            columns={this.state.columns}
                            defaultPageSize={this.state.pageSize}
                            getTrProps={this.onRowClick}
                            showPagination={true}
                            style={{
                                height: '300px'
                            }}
                        />
                    </Col>
                </Row>
            </div>
        )
    }
}

export default Table;
