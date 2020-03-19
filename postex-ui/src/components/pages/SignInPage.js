import * as React from "react";
import {ErrorMessage, Field, Form, Formik} from "formik";
import * as Yup from 'yup';
import {authenticationService} from "../../services";
import {Link} from "react-router-dom";

export class SignInPage extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        if (authenticationService.isSignedInSubject.value) {
            this.props.history.push('/');
        }
    }

    render() {
        return (
            <div className="row">
                <div className="col-md-6 offset-md-3">
                    <h2>Sign In</h2>
                    <Formik
                        initialValues={{
                            username: '',
                            password: ''
                        }}
                        validationSchema={Yup.object().shape({
                            username: Yup.string().required('Username is required'),
                            password: Yup.string().required('Password is required')
                        })}
                        onSubmit={({username, password}, {setStatus, setSubmitting}) => {
                            setStatus();
                            authenticationService.signIn(username, password)
                                .then(
                                    account => {
                                        const {from} = this.props.location.state || {from: {pathname: "/"}};
                                        this.props.history.push(from);
                                    },
                                    error => {
                                        setSubmitting(false);
                                        setStatus(error);
                                    }
                                );
                        }}
                        render={({errors, status, touched, isSubmitting}) => (
                            <Form>
                                <div className="form-group">
                                    <label htmlFor="username">Username</label>
                                    <Field name="username" type="text"
                                           className={'form-control' + (errors.username && touched.username ? ' is-invalid' : '')}/>
                                    <ErrorMessage name="username" component="div" className="invalid-feedback"/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="password">Password</label>
                                    <Field name="password" type="password"
                                           className={'form-control' + (errors.password && touched.password ? ' is-invalid' : '')}/>
                                    <ErrorMessage name="password" component="div" className="invalid-feedback"/>
                                </div>
                                <div className="form-group">
                                    <button type="submit" className="btn btn-primary" disabled={isSubmitting}>Sign In
                                    </button>
                                    {isSubmitting &&
                                    <img alt={'img'}
                                         src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///
                                      wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGgg
                                      YWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbE
                                      xwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEg
                                      G4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAA
                                      zYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIu
                                      mIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0r
                                      nZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82
                                      p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2n
                                      FwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7
                                      HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA=="/>
                                    }
                                    <Link style={{float: 'right'}} to="/signup">Sign Up</Link>
                                </div>
                                {status &&
                                <div className={'alert alert-danger'}>{status}</div>
                                }
                            </Form>
                        )}
                    />
                </div>
            </div>
        );
    }
}
