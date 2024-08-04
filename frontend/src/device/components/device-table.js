import React from "react";
import Table from "../../commons/tables/table.js";

const columns = [
    {
        Header: 'Id',
        accessor: 'id',
    },
    {
        Header: 'Description',
        accessor: 'description',
    },
    {
        Header: 'Address',
        accessor: 'address',
    },
    {
        Header: 'Maximum Hourly Energy Consumption',
        accessor: 'maxHourlyEnergyConsumption',
    },
    {
        Header: 'Client', 
        accessor: 'personId', 
    },
];

const filters = [
    {
        accessor: 'username', 
    }
];

class DeviceTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            tableData: this.props.tableData,
            selectedDeviceId: null,
        };
    }

    handleRowClick = (deviceId) => {
        this.setState({ selectedDeviceId: deviceId });
    };

    render() {
        return (
            <div className="divTransparent">
               <Table
                data={this.state.tableData}
                columns={columns}
                search={filters}
                pageSize={5}
                handleRowClick={this.handleRowClick}
            />
            </div>
        )
    }
}

export default DeviceTable;