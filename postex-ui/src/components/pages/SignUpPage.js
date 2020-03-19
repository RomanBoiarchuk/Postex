import * as React from "react";
import {ErrorMessage, Field, Form, Formik} from "formik";
import * as Yup from "yup";
import {authenticationService} from "../../services";
import {Link} from "react-router-dom";

export class SignUpPage extends React.Component {
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
                    <h2>Sign Up</h2>
                    <Formik
                        initialValues={
                            {
                                firstName: '',
                                lastName: '',
                                country: '',
                                city: '',
                                user: {
                                    username: '',
                                    password: ''
                                }
                            }}
                        validationSchema={Yup.object().shape({
                            firstName: Yup.string().required('First name is required'),
                            lastName: Yup.string().required('Last name is required'),
                            user: Yup.object().shape({
                                username: Yup.string().required('Username is required'),
                                password: Yup.string().required('Password is required')
                            })
                        })}
                        onSubmit={(account, {setStatus, setSubmitting}) => {
                            setStatus();
                            authenticationService.signUp(account)
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
                                    <label htmlFor="firstName">First Name</label>
                                    <Field name="firstName" type="text"
                                           className={'form-control' + (errors.firstName && touched.firstName ? ' is-invalid' : '')}/>
                                    <ErrorMessage name="firstName" component="div" className="invalid-feedback"/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="lastName">Last Name</label>
                                    <Field name="lastName" type="text"
                                           className={'form-control' + (errors.lastName && touched.lastName ? ' is-invalid' : '')}/>
                                    <ErrorMessage name="lastName" component="div" className="invalid-feedback"/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="country">Country</label>
                                    <Field name="country" type="text"
                                           className={'form-control' + (errors.country && touched.country ? ' is-invalid' : '')}/>
                                    <ErrorMessage name="country" component="div" className="invalid-feedback"/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="city">City</label>
                                    <Field name="city" type="text"
                                           className={'form-control' + (errors.city && touched.city ? ' is-invalid' : '')}/>
                                    <ErrorMessage name="city" component="div" className="invalid-feedback"/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="user.username">Username</label>
                                    <Field name="user.username" type="text"
                                           className={'form-control' + (errors.user && errors.user.username
                                           && touched.user && touched.user.username ? ' is-invalid' : '')}/>
                                    <ErrorMessage name="user.username" component="div" className="invalid-feedback"/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="user.password">Password</label>
                                    <Field name="user.password" type="password"
                                           className={'form-control' + (errors.user && errors.user.password
                                           && touched.user && touched.user.password ? ' is-invalid' : '')}/>
                                    <ErrorMessage name="user.password" component="div" className="invalid-feedback"/>
                                </div>
                                <div className="form-group">
                                    <button type="submit" className="btn btn-primary" disabled={isSubmitting}>Sign Up
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
                                    <Link style={{float: 'right'}} to="/signin">Sign In</Link>
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
