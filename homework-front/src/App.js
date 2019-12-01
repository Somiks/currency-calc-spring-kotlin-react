import React from 'react';
import './App.css';
import {Route, Switch, withRouter} from 'react-router-dom';

import Admin from './component/admin/Admin';
import Main from './component/main/Main';
import HeaderComponent from './component/header/HeaderComponent';

import {Layout} from 'antd';

const {Content, Footer} = Layout;

function App() {
    return (
        <Layout className="app-container">
            <HeaderComponent/>
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
