import './HeaderComponent.css';
import React, {Component} from 'react';
import {Link, withRouter} from 'react-router-dom';
import {Icon, Layout, Menu} from "antd";

const {Header} = Layout;

class HeaderComponent extends Component {

    render() {
        return (
            <Header>
                <div className="container">
                    <Menu
                        theme="dark"
                        mode="horizontal"
                        defaultSelectedKeys={['/']}
                        className="app-menu"
                        selectedKeys={[this.props.location.pathname]}
                    >
                        <Menu.Item key="/">
                            <Icon type="home"/>
                            <span>Home</span>
                            <Link to="/"/></Menu.Item>
                        <Menu.Item key="/admin">
                            <Icon type="profile"/>
                            <span>Admin</span>
                            <Link to="/admin"/>
                        </Menu.Item>
                    </Menu>
                </div>
            </Header>
        )
    }
}

export default withRouter(HeaderComponent);