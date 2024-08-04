import React, { Component } from "react";
import { getMeasurements } from "./api/measuremets-api";
import MeasurementsChart from "./chart";
import Notification from "./notification";

class MeasurementsContainer extends Component{

    constructor(props){
        super(props);

        this.state ={
            measurementsMap: new Map(),
            isLoading: true,
            alertMessages: props.alertMessages
        };

        this.fetchMeasurements();
    }

    componentDidUpdate() {
      console.log("mesajele sunt " + this.alertMessages)
    }

    componentDidMount() {
      //console.log("alertele din lista sunt " + this.alertMessages)
    }


    fetchMeasurements() {
        const token = sessionStorage.getItem('token');
        const selectedDeviceId = sessionStorage.getItem('selectedPersonId');
        console.log("selectedDeviceId " + selectedDeviceId);
    
        getMeasurements(selectedDeviceId, token, (result, status, err) => {
          if (status === 200) {
            console.log("result from request " +  JSON.stringify(result));
            this.setState({ measurementsMap: result, isLoading: false });
          } else {
            console.error("Failed to fetch measurements:", err);
            this.setState({ isLoading: false }); 
          }
        });
      }

     render() {
        const { measurementsMap, isLoading } = this.state;

        return (
            <div>
            {/* {this.props.alertMessage && <Notification message={this.props.alertMessage} />} */}
  
            {isLoading ? (
              <p>Loading...</p>
            ) : (
              
              <MeasurementsChart measurementsMap={measurementsMap}/>
            )}
          </div>
        );
  }


};

export default MeasurementsContainer;
