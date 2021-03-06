package com.algaworks.config;

import com.algaworks.config.token.CustomTokenEnhacer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

/**
 * Created by Bruno on 01/08/2017.
 */

@Profile("oauth-security")
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Qualifier("appUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("angular")
                .secret("$2a$10$dN3si4DkfRmtLbaFRv0/PODmpMqIQB2HrpUEJNMfAwOMqgS7wH.va") // @ngul@r0
                .scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(1800)
                .refreshTokenValiditySeconds(3600 * 24)
            .and()
                .withClient("mobile")
                .secret("$2a$10$znUVuhwD0goReA6gsnfUfeDZqIGlXn/5g6klI/g0yUZ4cMFneX1Ki") // m0b1l30 $2a$10$znUVuhwD0goReA6gsnfUfeDZqIGlXn/5g6klI/g0yUZ4cMFneX1Ki
                .scopes("read")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(56)
                .accessTokenValiditySeconds(1800)
                .refreshTokenValiditySeconds(3600 * 24);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhacer(), accessTokenConverter()));

        endpoints
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .reuseRefreshTokens(false)
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager);
    }

    @Bean
    public TokenEnhancer tokenEnhacer() {
        return new CustomTokenEnhacer();
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("algaworks");
        return accessTokenConverter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /*@Bean
    public MethodSecurityExpressionHandler createExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }*/
}
