import React from "react";
import Table from "../../commons/tables/table.js";

const columns = [
    {
        Header: 'Id',
        accessor: 'id',
    },
    {
        Header: 'Username',
        accessor: 'username',
    },
    {
        Header: 'Role',
        accessor: 'role',
    }
];

const filters = [
    {
        accessor: 'username',
    }
];

class PersonTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            tableData: this.props.tableData,
            selectedPersonId: null,
        };
    }

    handleRowClick = (personId) => {
        this.setState({ selectedPersonId: personId });
    };

    render() {
        return (
            <Table
                data={this.state.tableData}
                columns={columns}
                search={filters}
                pageSize={5}
                handleRowClick={this.handleRowClick}
            />
        );
    }
}

export default PersonTable;