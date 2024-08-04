import React from 'react';
import logo from './commons/images/icon1.png';
import { withRouter } from 'react-router'; 
import {
    DropdownItem,
    DropdownMenu,
    DropdownToggle,
    Nav,
    Navbar,
    NavbarBrand,
    NavLink,
    UncontrolledDropdown
} from 'reactstrap';

const textStyle = {
    color: 'white',
    textDecoration: 'none'
};

const NavigationBar = ({ location, history }) => {
    const userRole = sessionStorage.getItem('personRole');
    console.log("userRole: " + userRole);

    const handleLogout = () => {
        sessionStorage.clear();
        history.replace('/login');
    };

    return (
        <div>
            <Navbar color="dark" light expand="md">
                <NavbarBrand href="/">
                    <img src={logo} width={"50"} height={"35"} />
                </NavbarBrand>
                <Nav className="mr-auto" navbar>
                    <UncontrolledDropdown nav inNavbar>
                        <DropdownToggle style={textStyle} nav caret>
                            Menu
                        </DropdownToggle>
                        <DropdownMenu right>
                            {userRole === 'ADMIN' && (
                                <DropdownItem>
                                    <NavLink href="/person">Persons</NavLink>
                                    <NavLink href="/device">Devices</NavLink>
                                    <NavLink href="/admin-chat">Chat</NavLink>
                                </DropdownItem>
                            )}
                            {userRole === 'CLIENT' && (
                                <DropdownItem>
                                    <NavLink href="/device">Devices</NavLink>
                                    <NavLink href="/admin-chat">Chat</NavLink>
                                </DropdownItem>
                            )}
                            <DropdownItem onClick={handleLogout}>Logout</DropdownItem>
                        </DropdownMenu>
                    </UncontrolledDropdown>
                </Nav>
            </Navbar>
        </div>
    );
};

export default withRouter(NavigationBar); 
