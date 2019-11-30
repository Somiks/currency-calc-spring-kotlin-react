import React from 'react';
import './App.css';
import {
    Route,
    Switch,
    withRouter,
    Link
} from 'react-router-dom';

import Admin from './component/admin/Admin';
import Main from './component/main/Main';

import { Layout, Menu, Icon} from 'antd';

const { Header, Content, Footer } = Layout;

function App() {
  return (
      <Layout className="app-container">
          <Header>
              <div className="container">
                  <Menu
                      theme="dark"
                      mode="horizontal"
                      defaultSelectedKeys={['1']}
                      className="app-menu"
                  >
                      <Menu.Item key="1">
                          <Icon type="home" />
                          <span>Home</span>
                          <Link to="/" /></Menu.Item>
                      <Menu.Item key="2">
                          <Icon type="profile" />
                          <span>Admin</span>
                          <Link to="/admin" />
                      </Menu.Item>
                  </Menu>
              </div>
          </Header>
          <Content className="app-content">
              <div className="container">
                  <Switch>
                      <Route path="/admin"><Admin/></Route>
                      <Route path="/"><Main/></Route>
                  </Switch>
              </div>
          </Content>
          <Footer>©2019 Created by Arturs</Footer>
      </Layout>
  );
}

export default withRouter(App);
