import React from 'react';
import './notification.css'; // Assuming you save the styles in a file named Notification.css

const Notification = ({ message }) => {
  return (
    <div className="alert danger-alert">
      <div>{message}</div>
    </div>
  );
};

export default Notification;
